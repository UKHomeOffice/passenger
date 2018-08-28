package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class WicuController {

    private static final Logger logger = LoggerFactory.getLogger(WicuController.class);

    public static final int RETENTION_DAYS = 14;

    private final WicuService wicuService;

    public WicuController(WicuService wicuService) {
        this.wicuService = wicuService;
    }

    @PreAuthorize("hasRole('WICU') or hasRole('ADMIN')")
    @RequestMapping(value = "/wicu", method = RequestMethod.GET)
    public String list(Model model) {
        List<DailyWashView> files = wicuService.files(RETENTION_DAYS);
        model.addAttribute("files", zoned(files));
        return "wicu/wicu";
    }

    @PreAuthorize("hasRole('WICU') or hasRole('ADMIN')")
    @RequestMapping(value = "/wicu", method = RequestMethod.POST)
    public String generate(Authentication authentication, RedirectAttributes redirectAttributes) {
        wicuService.generate(SecurityUtil.username(), SecurityUtil.fullName());
        redirectAttributes.addFlashAttribute("generation_started", true);
        return "redirect:wicu";
    }

    @PreAuthorize("hasRole('WICU') or hasRole('ADMIN')")
    @RequestMapping(value = "/wicu/files/{id}/{type}", method = RequestMethod.GET)
    public void download(@PathVariable("id") String creationUuidStr, @PathVariable("type") String typeStr,
                         Authentication authentication, HttpServletResponse response) {
        UUID creationUuid = UUID.fromString(creationUuidStr);
        DailyWashContent.Type type = DailyWashContent.Type.valueOf(typeStr);
        DailyWashCreation dailyWashCreation = wicuService.get(creationUuid);

        try {
            final DailyWashContent dailyWashContent = wicuService.getFile(dailyWashCreation, type, SecurityUtil.username(),
                    SecurityUtil.fullName());
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", dailyWashContent.fileName));
            response.setHeader("Content-Length", Integer.toString(dailyWashContent.contentSizeInBytes));
            response.getOutputStream().write(dailyWashContent.content.getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (IOException e) {
            logger.info("Error writing getFile to output stream. id={} type={}", creationUuidStr, typeStr, e);
            throw new WicuException("IOError writing getFile to output stream");
        }
    }

    public List<ZonedDailyWashView> zoned(List<DailyWashView> dwv) {
        return dwv.stream().map(ZonedDailyWashView::new).collect(Collectors.toUnmodifiableList());
    }

    public static class ZonedDailyWashView {
        public final UUID uuid;
        public final String type;
        public final String filename;
        public final ZonedDateTime creationTime;
        public final String creatorUsername;
        public final String creatorFullName;
        public final int rows;
        public final ZonedDateTime lastDownloadTime;
        public final String lastDownloadUsername;
        public final String lastDownloadFullName;

        public ZonedDailyWashView(DailyWashView dailyWashView) {
            this.uuid = dailyWashView.uuid;
            this.type = dailyWashView.type;
            this.filename = dailyWashView.filename;
            this.creationTime = dailyWashView.creationTime.atZone(ZoneId.systemDefault());
            this.creatorUsername = dailyWashView.creatorUsername;
            this.creatorFullName = dailyWashView.creatorFullName;
            this.rows = dailyWashView.rows;
            this.lastDownloadTime = Optional.ofNullable(dailyWashView.lastDownloadTime).map(i -> i.atZone(ZoneId.systemDefault())).orElse(null);
            this.lastDownloadUsername = dailyWashView.lastDownloadUsername;
            this.lastDownloadFullName = dailyWashView.lastDownloadFullName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ZonedDailyWashView that = (ZonedDailyWashView) o;
            return rows == that.rows &&
                    Objects.equals(uuid, that.uuid) &&
                    Objects.equals(type, that.type) &&
                    Objects.equals(filename, that.filename) &&
                    Objects.equals(creationTime, that.creationTime) &&
                    Objects.equals(creatorUsername, that.creatorUsername) &&
                    Objects.equals(creatorFullName, that.creatorFullName) &&
                    Objects.equals(lastDownloadTime, that.lastDownloadTime) &&
                    Objects.equals(lastDownloadUsername, that.lastDownloadUsername) &&
                    Objects.equals(lastDownloadFullName, that.lastDownloadFullName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, type, filename, creationTime, creatorUsername, creatorFullName, rows, lastDownloadTime, lastDownloadUsername, lastDownloadFullName);
        }

        @Override
        public String toString() {
            return "ZonedDailyWashView{" +
                    "uuid=" + uuid +
                    ", type='" + type + '\'' +
                    ", filename='" + filename + '\'' +
                    ", creationTime=" + creationTime +
                    ", creatorUsername='" + creatorUsername + '\'' +
                    ", creatorFullName='" + creatorFullName + '\'' +
                    ", rows=" + rows +
                    ", lastDownloadTime=" + lastDownloadTime +
                    ", lastDownloadUsername='" + lastDownloadUsername + '\'' +
                    ", lastDownloadFullName='" + lastDownloadFullName + '\'' +
                    '}';
        }
    }
}
