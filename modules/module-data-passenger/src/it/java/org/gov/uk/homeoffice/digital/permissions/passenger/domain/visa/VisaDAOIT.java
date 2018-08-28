package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.PassengerDBITConfiguration;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PassengerDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class VisaDAOIT {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Test
    public void insertAndGetVisa() {

        Visa visa = new VisaBuilder()
                .setPassportNumber("ABC")
                .setValidFrom(LocalDate.of(2017, 12, 2))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("534MP6XH3")
                .setCatDEndorsements(emptyList())
                .setStatus(VisaStatus.REVOKED)
                .setReason("you lied!")
                .createVisa();

        dbi.useHandle(handle -> {
            VisaDAO dao = handle.attach(VisaDAO.class);
            Long id = dao.save(visa);
            Visa reloadedVisa = dao.getByPassportNumber("ABC");
            assertThat(reloadedVisa.withId(null), equalTo(visa));
            assertThat(reloadedVisa, not(sameInstance(visa)));
        });
    }

    @Test
    public void insertAndGetEndorsements() {


        Visa visa = new VisaBuilder()
                .setPassportNumber("ABC")
                .setValidFrom(LocalDate.of(2017, 12, 2))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("534MP6XH3")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .createVisa();

        dbi.useHandle(handle -> {
            VisaDAO dao = handle.attach(VisaDAO.class);
            Long id = dao.save(visa);
            dao.saveEndorsement("T4 G Student SPX Work limit 10 hrs,p/w term time.", id);
            dao.saveEndorsement("No Public Funds", id);
            List<String> reloadedEndorsements = dao.getEndorsementsForVisa(id);
            assertThat(reloadedEndorsements, is(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds")));
        });
    }

    @Test
    public void deleteEndorsements() {
        Visa visa = new VisaBuilder()
                .setPassportNumber("ABC")
                .setValidFrom(LocalDate.of(2017, 12, 2))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("534MP6XH3")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .createVisa();

        dbi.useHandle(handle -> {
            VisaDAO dao = handle.attach(VisaDAO.class);
            Long id = dao.save(visa);
            dao.saveEndorsement("T4 G Student SPX Work limit 10 hrs,p/w term time.", id);
            dao.saveEndorsement("No Public Funds", id);
            dao.deleteEndorsementsForVisa(id);
            List<String> reloadedEndorsements = dao.getEndorsementsForVisa(id);
            assertThat(reloadedEndorsements, is(emptyList()));
        });
    }

    @Test
    public void doesNotExist(){
        dbi.useHandle(handle -> {
            VisaDAO dao = handle.attach(VisaDAO.class);
            assertThat(dao.exists("ABC"), is(nullValue()));
        });
    }

    @Test
    public void exists(){
        Visa visa = new VisaBuilder()
                .setPassportNumber("ABC")
                .setValidFrom(LocalDate.of(2017, 12, 2))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("534MP6XH3")
                .createVisa();

        dbi.useHandle(handle -> {
            VisaDAO dao = handle.attach(VisaDAO.class);
            final Long id = dao.save(visa);
            assertThat(dao.exists("ABC"), is(id));
        });
    }

    @Test
    public void update(){

        Visa visa = new VisaBuilder()
                .setPassportNumber("ABC")
                .setValidFrom(LocalDate.of(2017, 12, 2))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setCatDEndorsements(emptyList())
                .setSpx("534MP6XH3")
                .setStatus(VisaStatus.VALID)
                .createVisa();

        dbi.useHandle(handle -> {
            VisaDAO dao = handle.attach(VisaDAO.class);
            final Long id = dao.save(visa);
            final Visa updatedVisa = visa.withSpx("new spx").withId(id).withStatus(VisaStatus.REVOKED).withReason("you lied!");
            dao.update(updatedVisa);
            assertThat(dao.getByPassportNumber("ABC"), is(updatedVisa));
        });
    }
}

