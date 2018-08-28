package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TravelBuilderTest {

    @Test
    public void shouldBuildTravel() {
        final LocalDateTime time = LocalDateTime.now();

        Travel travel = new TravelBuilder()
                .setFlightNumber("flightNumber")
                .setDeparturePoint("departurePoint")
                .setDepartureTime(time)
                .setArrivalPoint("arrivalPoint")
                .setArrivalTime(time)
                .createTravel();

        assertThat(travel.flightNumber, is("flightNumber"));
        assertThat(travel.departurePoint, is("departurePoint"));
        assertThat(travel.departureTime, is(time));
        assertThat(travel.arrivalPoint, is("arrivalPoint"));
        assertThat(travel.arrivalTime, is(time));
    }

}