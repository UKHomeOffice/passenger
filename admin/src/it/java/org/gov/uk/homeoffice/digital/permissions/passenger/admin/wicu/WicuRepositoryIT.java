package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.AdminDBITConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashContent.Type.DOC;
import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashContent.Type.NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AdminDBITConfiguration.class})
@TruncateTablesBeforeEachTest
public class WicuRepositoryIT {

    @Autowired
    private WicuRepository wicuRepository;

    @Test
    public void saveAndLoadDailyWashCreation() {
        DailyWashCreation dwc = new DailyWashCreation(UUID.randomUUID(), Instant.now().truncatedTo(ChronoUnit.SECONDS), 2,
                "username", "docfname", "namefname", "fullName");
        DailyWashCreation saved = wicuRepository.save(dwc);

        DailyWashCreation loaded = wicuRepository.get(saved.id);

        assertThat(loaded, is(saved));
        assertThat(loaded, not(sameInstance(saved)));
    }

    @Test
    public void saveAndLoadDailyWashCreationWithNullFiles() {
        DailyWashCreation dwc = new DailyWashCreation(UUID.randomUUID(), Instant.now().truncatedTo(ChronoUnit.SECONDS), 0,
                "username",null,null, "fullName");
        DailyWashCreation saved = wicuRepository.save(dwc);

        DailyWashCreation loaded = wicuRepository.get(saved.id);

        assertThat(loaded, is(saved));
        assertThat(loaded, not(sameInstance(saved)));
    }

    @Test
    public void saveDailyWashDownload() {
        DailyWashCreation dwc = new DailyWashCreation(UUID.randomUUID(), Instant.now().minusSeconds(10).truncatedTo(ChronoUnit.SECONDS), 1,
                "creator-username",
                "documentCheckFilename", "nameCheckFilename",
                "creator fullName");
        DailyWashCreation creation = wicuRepository.save(dwc);

        DailyWashDownload download = new DailyWashDownload(creation.id, NAME,
                "download-username", "download fullName", Instant.now().truncatedTo(ChronoUnit.SECONDS));
        wicuRepository.log(download);

        List<DailyWashView> view = wicuRepository.getLatest(2);

        assertThat(view, containsInAnyOrder(
                new DailyWashView(creation.uuid, DOC.toString(), creation.documentCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, null, null, null),
                new DailyWashView(creation.uuid, NAME.toString(), creation.nameCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, download.downloadTime, download.username, download.fullName)));
    }

    @Test
    public void saveDailyWashMultipleDownloads() {
        DailyWashCreation dwc = new DailyWashCreation(UUID.randomUUID(), Instant.now().minusSeconds(10).truncatedTo(ChronoUnit.SECONDS), 1,
                "creator-username",
                "documentCheckFilename", "nameCheckFilename",
                "creator fullName");
        DailyWashCreation creation = wicuRepository.save(dwc);

        DailyWashDownload download0 = new DailyWashDownload(creation.id, NAME,
                "download-username0", "download fullName0", Instant.now().minusSeconds(5).truncatedTo(ChronoUnit.SECONDS));
        wicuRepository.log(download0);
        DailyWashDownload latestDownload = new DailyWashDownload(creation.id, NAME,
                "download-username", "download fullName", Instant.now().truncatedTo(ChronoUnit.SECONDS));
        wicuRepository.log(latestDownload);

        List<DailyWashView> view = wicuRepository.getLatest(2);

        assertThat(view, containsInAnyOrder(
                new DailyWashView(creation.uuid, DOC.toString(), creation.documentCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, null, null, null),
                new DailyWashView(creation.uuid, NAME.toString(), creation.nameCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, latestDownload.downloadTime, latestDownload.username, latestDownload.fullName)));
    }
}
