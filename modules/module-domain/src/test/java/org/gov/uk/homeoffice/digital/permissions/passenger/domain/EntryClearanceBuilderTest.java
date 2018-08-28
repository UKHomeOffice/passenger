package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryClearanceBuilderTest {

    @Test
    public void shouldBuildEntryClearance() {
        final LocalDate date = LocalDate.now();

        final EntryClearance entryClearance = new EntryClearanceBuilder()
                .setPassportNumber("123ABC456D")
                .setStartDate(date)
                .setEndDate(date)
                .setPassportNationality("nationality")
                .setSurname("surname")
                .setOtherNames("otherNames")
                .setTierType("tierType")
                .setDateOfBirth(date)
                .setVafNumber("vafNumber")
                .setCasNumber("casNumber")
                .setSpxNumber("spxNumber")
                .setVisaValidToDate(date)
                .setConditionsLine1("conditionsLine1")
                .setConditionsLine2("conditionsLine2")
                .createEntryClearance();

        assertThat(entryClearance.getPassportNumber(), is("123ABC456D"));
        assertThat(entryClearance.getStartDate(), is(date));
        assertThat(entryClearance.getEndDate(), is(date));
        assertThat(entryClearance.getPassportNationality(), is("nationality"));
        assertThat(entryClearance.getSurname(), is("surname"));
        assertThat(entryClearance.getOtherNames(), is("otherNames"));
        assertThat(entryClearance.getTierType(), is("tierType"));
        assertThat(entryClearance.getDateOfBirth(), is(date));
        assertThat(entryClearance.getVafNumber(), is("vafNumber"));
        assertThat(entryClearance.getCasNumber(), is("casNumber"));
        assertThat(entryClearance.getSpxNumber(), is("spxNumber"));
        assertThat(entryClearance.getVisaValidToDate(), is(date));
        assertThat(entryClearance.getConditionsLine1(), is("conditionsLine1"));
        assertThat(entryClearance.getConditionsLine2(), is("conditionsLine2"));
    }

}