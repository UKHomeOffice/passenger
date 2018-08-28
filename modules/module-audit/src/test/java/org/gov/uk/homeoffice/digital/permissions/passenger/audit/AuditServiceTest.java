package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Jdbi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AuditServiceTest {

    @Mock
    Jdbi dbi;

    @Mock
    AuditDAO auditDAO;

    @Mock
    MeterRegistry meterRegistry;

    @Spy
    Counter counter;

    @InjectMocks
    private AuditService testObject;

    Collection<Audit> auditCollection = new ArrayList<>();

    String csv = "\"Event ID\",\"UserName\",\"Action\",\"Outcome\",\"Date and time\"\n" +
            "\"1\",\"passenger-admin\",\"VIEW_REPORT\",\"OK\",\"2017-10-10T00:00\"\n" +
            "\"1\",\"passenger-admin\",\"VIEW_REPORT\",\"DENIED\",\"2017-10-10T00:00\"";

    String csvNoData = "\"Event ID\",\"UserName\",\"Action\",\"Outcome\",\"Date and time\"\n" +
            "\"No data is available for this period.\"";

    LocalDateTime from = LocalDateTime.of(2017, 10, 1, 0, 0);
    LocalDateTime to = LocalDateTime.of(2017, 11, 1, 0, 0);

    Audit audit1;
    Audit audit2;

    @Before
    public void setUp() throws Exception {

        when(dbi.onDemand(AuditDAO.class)).thenReturn(auditDAO);
        when(meterRegistry.counter("error", "audit")).thenReturn(counter);

        audit1 = new Audit(1l, "passenger-admin", LocalDateTime.of(2017, 10, 10, 0, 0), "OK", "VIEW_REPORT");
        audit2 = new Audit(1l, "passenger-admin", LocalDateTime.of(2017, 10, 10, 0, 0), "DENIED", "VIEW_REPORT");

        auditCollection.add(audit1);
        auditCollection.add(audit2);
    }

    @Test
    public void exportAuditLog() throws Exception {

        when(auditDAO.selectInRange("01102017","01112017")).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(10, 2017);

        assertEquals(csv, log.trim());
    }

    @Test
    public void allowUserToDownloadSixMonthOfData() throws Exception {

        LocalDateTime from = LocalDateTime.of(2017, 01, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2017, 07, 2, 0, 0);

        final String user = "passenger-admin";

        when(auditDAO.selectInRangeForUser("01012017","02072017", user)).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.toLocalDate(), user, null);

        assertEquals(csv, log.trim());
    }

    @Test
    public void doNotAllowUserToDownloadMoreThanSixMonthOfData() throws Exception {

        LocalDateTime from = LocalDateTime.of(2017, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2017, 8, 1, 0, 0);

        final String user = "passenger-admin";

        when(auditDAO.selectInRangeForUser("01012017","02082017", user)).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.toLocalDate(), user, null);

        assertEquals(csvNoData, log.trim());

    }

    @Test
    public void exportAuditLogForUser() throws Exception {
        final String user = "passenger-admin";

        when(auditDAO.selectInRangeForUser("01102017","01112017", user)).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.toLocalDate(), user, null);

        assertEquals(csv, log.trim());
    }

    @Test
    public void exportAuditLogForUserWhenUserIsNull() throws Exception {
        final String user = null;
        final String team = "team";

        when(auditDAO.selectInRangeForTeam("01102017","01112017", getValueForLikeQuery(team))).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.toLocalDate(), user, team);

        assertEquals(csv, log.trim());
    }

    @Test
    public void exportAuditLogForUserAndTeam() throws Exception {
        final String user = "user";
        final String team = "team";

        when(auditDAO.selectInRangeForUserAndTeam("01102017","01112017", user, getValueForLikeQuery(team))).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.toLocalDate(), user, team);

        assertEquals(csv, log.trim());
    }

    @Test
    public void exportAuditLogForUserAndTeamWithDateRange() throws Exception {
        final String user = "user";
        final String team = "team";

        when(auditDAO.selectInRangeForUserAndTeam("01102017","01112018", user, getValueForLikeQuery(team))).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.plusYears(1).toLocalDate(), user, team);

        assertEquals(csvNoData, log.trim());
    }

    @Test
    public void exportAuditLogForUserWhenUserANdTeamAreNull() throws Exception {
        final String user = null;

        when(auditDAO.selectInRange("01102017","01112017")).thenReturn(auditCollection);

        final String log = testObject.exportAuditLog(from.toLocalDate(), to.toLocalDate(), null, null);

        assertEquals(csv, log.trim());
    }

    private String getValueForLikeQuery(String teamName) {
        return "%" + teamName + "%";
    }
}
