package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.model;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantModelAdapterTest {

    @InjectMocks
    private ParticipantModelAdapter underTest;

    @Test
    public void shouldConvertDomainToUIModel() {
        ParticipantModel model = underTest.from(participantStub(), visaStub(), true);
        assertThat(model, notNullValue());
        assertThat(model.getId(), is(55555L));
        assertThat(model.getGwf(), is("gwf"));
        assertThat(model.getVaf(), is("vaf"));
        assertThat(model.getCas(), is("cas"));
        assertThat(model.getFirstName(), is("firstName"));
        assertThat(model.getMiddleName(), is("middleName"));
        assertThat(model.getSurName(), is("surName"));
        assertThat(model.getDateOfBirth(), is("1 January 1999"));
        assertThat(model.getNationality(), is("nationality"));
        assertThat(model.getPassportNumber(), is("11111111"));
        assertThat(model.getMobileNumber(), is("07555555555"));
        assertThat(model.getEmail(), is("test@email.com"));
        assertThat(model.getInstitutionAddress(), is("institution address"));
        assertThat(model.getValidFrom(), is("1 January 2010"));
        assertThat(model.getValidTo(), is("10 January 2014"));
        assertThat(model.getSpx(), is("spx"));
        assertThat(model.getCatDEndorsements(), is("endorsement"));
        assertThat(model.getVisaStatus(), is(VisaStatus.ISSUED));
        assertThat(model.getReason(), is("reason"));
        assertThat(model.isUpdated(), is(true));
        assertThat(model.getUpdatedBy(), is("updatedBy"));
    }

    private Participant participantStub() {
        return new Participant(
                55555L,
                "gwf",
                "vaf",
                "cas",
                null,
                "firstName",
                "middleName",
                "surName",
                LocalDate.of(1999, 1, 1),
                "nationality",
                "11111111",
                "07555555555",
                "test@email.com",
                "institution address",
                Collections.singleton("emails"),
                "updatedBy",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Visa visaStub() {
        return new Visa(
                9999L,
                "11111111",
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2014, 1, 10),
                "spx",
                Collections.singletonList("endorsement"),
                VisaStatus.ISSUED,
                "action",
                "reason",
                "Tier 4 Visa"
        );
    }

}