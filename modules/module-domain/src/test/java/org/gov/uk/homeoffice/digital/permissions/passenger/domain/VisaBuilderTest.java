package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VisaBuilderTest {

    @Test
    public void shouldBuildVisa() {
        LocalDate date = LocalDate.now();

        Visa visa = new VisaBuilder()
                .setId(2120L)
                .setPassportNumber("passportNumber")
                .setValidFrom(date)
                .setValidTo(date)
                .setSpx("spx")
                .setCatDEndorsements(Collections.singletonList("catDEndorsements"))
                .setStatus(VisaStatus.ISSUED)
                .setReason("reason")
                .createVisa();

        assertThat(visa.getId(), is(2120L));
        assertThat(visa.getPassportNumber(), is("passportNumber"));
        assertThat(visa.getValidFrom(), is(date));
        assertThat(visa.getValidTo(), is(date));
        assertThat(visa.getSpx(), is("spx"));
        assertThat(visa.getCatDEndorsements(), is(Collections.singletonList("catDEndorsements")));
        assertThat(visa.getStatus(), is(VisaStatus.ISSUED));
        assertThat(visa.getReason(), is("reason"));
    }

}