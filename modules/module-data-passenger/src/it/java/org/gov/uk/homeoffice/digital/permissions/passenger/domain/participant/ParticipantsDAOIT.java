package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.ParticipantBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.PassengerDBITConfiguration;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.CollectionUtils.set;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PassengerDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class ParticipantsDAOIT {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Test
    public void insertAndGetParticipant() {
        Participant participant = participantBuilder.createParticipant();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant);
            Participant reloadedParticipant = dao.getById(1L);
            assertThat(reloadedParticipant, equalTo(participant));
            assertThat(reloadedParticipant, not(sameInstance(participant)));
            assertThat(reloadedParticipant.getCreated(), not(nullValue()));
            assertThat(reloadedParticipant.getUpdated(), not(nullValue()));
        });
    }

    @Test
    public void deleteParticipant() {
        final String passportNumber = "539701015";
        Participant participant = participantBuilder
                .setPassportNumber(passportNumber)
                .createParticipant();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant);
            assertThat(dao.getById(1L), is(not(nullValue())));
            dao.delete(passportNumber);
            assertThat(dao.getById(1L), is(nullValue()));
        });
    }

    @Test
    public void updateParticipant() {
        Participant participant = participantBuilder.createParticipant();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant);

            Participant createdParticipant = dao.getById(1L);
            assertThat(createdParticipant.getCreated(), not(nullValue()));
            assertThat(createdParticipant.getUpdated(), not(nullValue()));
            assertThat(createdParticipant.getUpdated(), is(createdParticipant.getCreated()));

            final Participant updatedParticipant = participant.withSurname("new name");
            dao.update(updatedParticipant);

            Participant reloadedParticipant = dao.getById(1L);
            assertThat(reloadedParticipant, equalTo(updatedParticipant));
            assertThat(reloadedParticipant.getCreated(), is(createdParticipant.getCreated()));
            assertThat(reloadedParticipant.getUpdated(), not(nullValue()));
            assertThat(reloadedParticipant.getUpdated(), is(not(createdParticipant.getUpdated())));

            final Participant updatedParticipant2 = participant.withSurname("another name");
            dao.update(updatedParticipant2);

            Participant reloadedParticipant2 = dao.getById(1L);
            assertThat(reloadedParticipant2.getUpdated(), is(not(reloadedParticipant.getUpdated())));
        });
    }

    @Test
    public void updateEmailsSent() {
        Participant participant = participantBuilder
                .setEmailsSent(set("GRANTED"))
                .createParticipant();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant);

            Participant revokedParticipant = participant.withEmailsSent(set("GRANTED", "REVOKED"));
            dao.setEmailsSent(revokedParticipant);

            assertThat(dao.getById(1L).getEmailsSent(), containsInAnyOrder("GRANTED", "REVOKED"));
        });
    }

    @Test
    public void doesNotExist() {
        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            assertThat(dao.exists(1L), is(nullValue()));
        });
    }

    @Test
    public void exists() {
        Participant participant = participantBuilder.withDefaults().createParticipant();
        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant);
            assertThat(dao.exists(1L), is(1L));
        });
    }

    @Test
    public void getAll() {
        Participant participant1 = participantBuilder.createParticipant();

        Participant participant2 = new ParticipantBuilder()
                .setId(2L)
                .setGwf("GWF046063548")
                .setVaf("1295750")
                .setCas("E4G0JJ1F30P0V4")
                .setFirstName("Karen Anne")
                .setSurName("Zeta")
                .setDateOfBirth(LocalDate.of(1997, 3, 6))
                .setNationality("USA")
                .setPassportNumber("531939666")
                .setMobileNumber("5089511730")
                .setEmail("test2@test.com")
                .setEmailsSent(Collections.emptySet())
                .createParticipant();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant1);
            dao.save(participant2);

            assertThat(dao.getAll(), is(asList(participant1, participant2)));
        });
    }

    @Test
    public void selectByVisaValidFromBetweenDates() {
        Participant participant = participantBuilder.createParticipant();

        LocalDate validFrom = LocalDate.of(2018, 4, 1);

        Visa visa = new VisaBuilder()
                .setPassportNumber(participant.getPassportNumber())
                .setValidFrom(validFrom)
                .setValidTo(LocalDate.of(2018, 12, 1))
                .setSpx("534MP6XH3")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .createVisa();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            VisaDAO visaDAO = handle.attach(VisaDAO.class);
            dao.save(participant);
            visaDAO.save(visa);

            // ---[----]-x- validFrom after range
            assertThat(dao.getByVisaValidFrom(validFrom.minusDays(4), validFrom.minusDays(2)), is(empty()));
            // ---[----x--- validFrom == range upperLimit
            assertThat(dao.getByVisaValidFrom(validFrom.minusDays(2), validFrom), contains(participant));
            // ---[-x--]--- validFrom within range
            assertThat(dao.getByVisaValidFrom(validFrom.minusDays(2), validFrom.plusDays(2)), contains(participant));
            // ---x----]--- validFrom == range lowerLimit
            assertThat(dao.getByVisaValidFrom(validFrom, validFrom.plusDays(2)), contains(participant));
            // -x-[----]--- validFrom before range
            assertThat(dao.getByVisaValidFrom(validFrom.plusDays(2), validFrom.plusDays(4)), is(empty()));
        });
    }

    @Test
    public void getByPassportNumberAndDateOfBirth() {

        final String passportNumber = "539701015";
        final LocalDate dateOfBirth = LocalDate.of(1996, 10, 22);

        Participant participant = participantBuilder
                .setDateOfBirth(dateOfBirth)
                .setPassportNumber(passportNumber)
                .createParticipant();

        dbi.useHandle(handle -> {
            ParticipantDAO dao = handle.attach(ParticipantDAO.class);
            dao.save(participant);
            assertThat(dao.getByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth), is(participant));
            assertThat(dao.getByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth.plusDays(1)), is(nullValue()));
            assertThat(dao.getByPassportNumberAndDateOfBirth(passportNumber + "1", dateOfBirth), is(nullValue()));
        });
    }

    private final ParticipantBuilder participantBuilder = new ParticipantBuilder()
            .setId(1L)
            .setGwf("GWF046027237")
            .setVaf("1295789")
            .setCas("E4G8DF0F36F0V1")
            .setFirstName("Tim Spencer")
            .setMiddleName("Bartholomew")
            .setSurName("Gama")
            .setDateOfBirth(LocalDate.of(1996, 10, 22))
            .setNationality("USA")
            .setPassportNumber("539701015")
            .setMobileNumber("6316032284")
            .setEmail("test1@test.com")
            .setInstitutionAddress("74 Academia Row, Haytown, Surrey, AB0 1CD")
            .setEmailsSent(set("GRANTED", "REVOKED"));
}
