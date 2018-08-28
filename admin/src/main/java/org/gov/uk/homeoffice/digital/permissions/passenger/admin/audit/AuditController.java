package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.annotation.Audit;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.annotation.AuditAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

import static org.gov.uk.homeoffice.digital.permissions.passenger.audit.util.URLUtil.decodeUtf8;

@Controller
public class AuditController {
    private static final String AUDIT_LOG_EXPORT_CSV = "auditLogExport.csv";
    private static final String CONTENT_TYPE = "text/csv";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    @Qualifier("audit.admin")
    private AuditService auditService;


    @PreAuthorize("hasRole('AUDIT')")
    @Audit(auditAction = AuditAction.DOWNLOAD, message = "Downloaded audit")
    @RequestMapping(value = "/audit/log", method = RequestMethod.POST, headers = "Accept=text/csv")
    public ResponseEntity<InputStreamResource> exportAuditAsCsv(@RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                                @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                                                                @RequestParam(value = "userName", required = false) String userName,
                                                                @RequestParam(value = "teamName", required = false) String teamName) {
        LOGGER.info("Download log for user: {} from :{} to: {}", userName, from, to);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", AUDIT_LOG_EXPORT_CSV);
        headers.add("Content-Type", CONTENT_TYPE);

        try {
            final String contents = auditService.exportAuditLog(from.toLocalDate(), to.toLocalDate(), decodeUtf8(userName), decodeUtf8(teamName));
            return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(contents.getBytes())),
                    headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Unable to export audit log.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Unable to export audit log.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
