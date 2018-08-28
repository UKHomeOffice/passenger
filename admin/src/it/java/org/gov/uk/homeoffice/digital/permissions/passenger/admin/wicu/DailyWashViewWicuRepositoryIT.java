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
import static org.hamcrest.Matchers.containsInAnyOrder;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AdminDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class DailyWashViewWicuRepositoryIT {

    @Autowired
    private WicuRepository wicuRepository;

    @Test
    public void oneCreationNoDownloads() {
        DailyWashCreation dwc = new DailyWashCreation(
                UUID.randomUUID(), Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.SECONDS), 2, "creator1",
                "docfname1", "namefname1", "full name");
        DailyWashCreation creation = wicuRepository.save(dwc);

        List<DailyWashView> rows = wicuRepository.getLatest(10);

        assertThat(rows, containsInAnyOrder(
                new DailyWashView(creation.uuid, DOC.toString(), creation.documentCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, null, null, null),
                new DailyWashView(creation.uuid, NAME.toString(), creation.nameCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, null, null, null))
        );
    }

    @Test
    public void oneCreationOneDownloadOfOneFile() {
        DailyWashCreation dwc = new DailyWashCreation(UUID.randomUUID(), Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.SECONDS), 2,
                "creator1", "docfname1", "namefname1", "creation fullname");
        DailyWashCreation creation = wicuRepository.save(dwc);

        DailyWashDownload download = new DailyWashDownload(creation.id, NAME, "downloadUser1", "download fullname", Instant.now().truncatedTo(ChronoUnit.SECONDS));
        wicuRepository.log(download);

        List<DailyWashView> rows = wicuRepository.getLatest(10);

        assertThat(rows, containsInAnyOrder(
                new DailyWashView(creation.uuid, DOC.toString(), creation.documentCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, null, null, null),
                new DailyWashView(creation.uuid, NAME.toString(), creation.nameCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, download.downloadTime, download.username, download.fullName))
        );
    }

    @Test
    public void oneCreationOneDownloadOfBothFiles() {
        DailyWashCreation dwc = new DailyWashCreation(
                UUID.randomUUID(), Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.SECONDS), 2, "creator1",
                "docfname1", "namefname1", "creation fullname");
        DailyWashCreation creation = wicuRepository.save(dwc);

        DailyWashDownload nameDownload = new DailyWashDownload(creation.id, NAME, "downloadUser1", "download fullname1", Instant.now().minusSeconds(5).truncatedTo(ChronoUnit.SECONDS));
        wicuRepository.log(nameDownload);
        DailyWashDownload docDownload = new DailyWashDownload(creation.id, DOC, "downloadUser2", "download fullname2", Instant.now().truncatedTo(ChronoUnit.SECONDS));
        wicuRepository.log(docDownload);

        List<DailyWashView> rows = wicuRepository.getLatest(10);

        assertThat(rows, containsInAnyOrder(
                new DailyWashView(creation.uuid, DOC.toString(), creation.documentCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, docDownload.downloadTime, docDownload.username, docDownload.fullName),
                new DailyWashView(creation.uuid, NAME.toString(), creation.nameCheckFilename, creation.creationTime,
                        creation.creatorUsername, creation.fullName, creation.rows, nameDownload.downloadTime, nameDownload.username, nameDownload.fullName))
        );
    }

    @Test
    public void oneCreationTwoDownloadsOfOneFile() {
        DailyWashCreation dwc = new DailyWashCreation(UUID.randomUUID(), Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.SECONDS), 2,
                "creator1", "docfname1", "namefname1", "creation fullname1");
        DailyWashCreation creation =wicuRepository.save(dwc);

        Instant lastDownloadTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        wicuRepository.log(new DailyWashDownload(creation.id, NAME, "another-downloadUser",
                "another download fullname", lastDownloadTime.minusSeconds(30).truncatedTo(ChronoUnit.SECONDS)));
        DailyWashDownload nameLastDownload = new DailyWashDownload(creation.id, NAME, "downloadUser",
                "download fullname", lastDownloadTime);
        wicuRepository.log(nameLastDownload);

        List<DailyWashView> rows = wicuRepository.getLatest(10);

        assertThat(rows, containsInAnyOrder(
                new DailyWashView(creation.uuid, DOC.toString(), creation.documentCheckFilename, creation.creationTime, creation.creatorUsername, creation.fullName,
                        creation.rows, null, null, null),
                new DailyWashView(creation.uuid, NAME.toString(), creation.nameCheckFilename, creation.creationTime, creation.creatorUsername, creation.fullName,
                        creation.rows, nameLastDownload.downloadTime, nameLastDownload.username, nameLastDownload.fullName)
        ));
    }
}
