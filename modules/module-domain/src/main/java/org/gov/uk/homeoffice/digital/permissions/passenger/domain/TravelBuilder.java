package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.time.LocalDateTime;

public class TravelBuilder {

    private String flightNumber;
    private String departurePoint;
    private LocalDateTime departureTime;
    private String arrivalPoint;
    private LocalDateTime arrivalTime;

    public TravelBuilder setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public TravelBuilder setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
        return this;
    }

    public TravelBuilder setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public TravelBuilder setArrivalPoint(String arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
        return this;
    }

    public TravelBuilder setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public Travel createTravel() {
        return new Travel(flightNumber, departurePoint, departureTime, arrivalPoint, arrivalTime);
    }
}