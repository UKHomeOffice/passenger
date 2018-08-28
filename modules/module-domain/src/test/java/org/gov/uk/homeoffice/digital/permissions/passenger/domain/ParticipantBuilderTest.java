package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParticipantBuilderTest {

    @Test
    public void shouldBuildParticipant() {
        LocalDate date = LocalDate.now();
        LocalDateTime time = LocalDateTime.now();

        Participant participant = new ParticipantBuilder()
                .setId(101L)
                .setGwf("gwf")
                .setVaf("vaf")
                .setCas("cas")
                .setFirstName("firstName")
                .setMiddleName("middleName")
                .setSurName("surName")
                .setDateOfBirth(date)
                .setNationality("nationality")
                .setPassportNumber("passportNumber")
                .setMobileNumber("mobileNumber")
                .setEmail("email")
                .setInstitutionAddress("institutionAddress")
                .setEmailsSent(Collections.singleton("emailsSent"))
                .setUpdatedBy("updatedBy")
                .setCreated(time)
                .setUpdated(time)
                .createParticipant();

        assertThat(participant.getId(), is(101L));
        assertThat(participant.getGwf(), is("gwf"));
        assertThat(participant.getVaf(), is("vaf"));
        assertThat(participant.getCas(), is("cas"));
        assertThat(participant.getFirstName(), is("firstName"));
        assertThat(participant.getMiddleName(), is("middleName"));
        assertThat(participant.getSurName(), is("surName"));
        assertThat(participant.getDateOfBirth(), is(date));
        assertThat(participant.getNationality(), is("nationality"));
        assertThat(participant.getPassportNumber(), is("passportNumber"));
        assertThat(participant.getMobileNumber(), is("mobileNumber"));
        assertThat(participant.getEmail(), is("email"));
        assertThat(participant.getInstitutionAddress(), is("institutionAddress"));
        assertThat(participant.getEmailsSent().toArray()[0], is("emailsSent"));
        assertThat(participant.getUpdatedBy(), is("updatedBy"));
        assertThat(participant.getCreated(), is(time));
        assertThat(participant.getUpdated(), is(time));
    }

}