package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.PassengerDBITConfiguration;
import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Gender;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PassengerDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class CrsRecordDAOIT {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Test
    public void selectValidWithinARange() {

        final LocalDate lowerLimitIncluded = LocalDate.now().minusDays(5);
        final LocalDate upperLimitIncluded = LocalDate.now().minusDays(2);

        dbi.useHandle(handle -> {
            final CrsRecordDAO dao = handle.attach(CrsRecordDAO.class);

            dao.save(defaultCrsRecord(LocalDate.now().minusDays(6)));
            dao.save(defaultCrsRecord(LocalDate.now().minusDays(4)));
            dao.save(defaultCrsRecord(LocalDate.now().minusDays(3)));
            dao.save(defaultCrsRecord(LocalDate.now().minusDays(1)));

            Collection<CrsRecord> results = dao.getValidWithinRange(lowerLimitIncluded, upperLimitIncluded);
            assertThat(results.size(), is(2));
        });

    }

    private CrsRecord defaultCrsRecord(LocalDate validFrom) {
        return CrsRecord.builder()
                .gwfRef("gwf_ref")
                .vafNo("vaf_no")
                .gender(Gender.MALE)
                .postName("post_name")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .nationality("USA")
                .passportNumber("passport_no")
                .validTo(LocalDate.now().plusDays(31))
                .validFrom(validFrom)
                .status(VisaStatus.ISSUED)
                .build();
    }

}