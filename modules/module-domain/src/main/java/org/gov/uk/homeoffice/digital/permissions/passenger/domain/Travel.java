package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Travel  implements Serializable {
    public final String flightNumber;
    public final String departurePoint;
    public final LocalDateTime departureTime;
    public final String arrivalPoint;
    public final LocalDateTime arrivalTime;

    public Travel(String flightNumber, String departurePoint, LocalDateTime departureTime, String arrivalPoint, LocalDateTime arrivalTime) {

        this.flightNumber = flightNumber;
        this.departurePoint = departurePoint;
        this.departureTime = departureTime;
        this.arrivalPoint = arrivalPoint;
        this.arrivalTime = arrivalTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Travel travel = (Travel) o;

        if (flightNumber != null ? !flightNumber.equals(travel.flightNumber) : travel.flightNumber != null)
            return false;
        if (departurePoint != null ? !departurePoint.equals(travel.departurePoint) : travel.departurePoint != null)
            return false;
        if (departureTime != null ? !departureTime.equals(travel.departureTime) : travel.departureTime != null)
            return false;
        if (arrivalPoint != null ? !arrivalPoint.equals(travel.arrivalPoint) : travel.arrivalPoint != null)
            return false;
        return arrivalTime != null ? arrivalTime.equals(travel.arrivalTime) : travel.arrivalTime == null;
    }

    @Override
    public int hashCode() {
        int result = flightNumber != null ? flightNumber.hashCode() : 0;
        result = 31 * result + (departurePoint != null ? departurePoint.hashCode() : 0);
        result = 31 * result + (departureTime != null ? departureTime.hashCode() : 0);
        result = 31 * result + (arrivalPoint != null ? arrivalPoint.hashCode() : 0);
        result = 31 * result + (arrivalTime != null ? arrivalTime.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Travel{" +
                "flightNumber='" + flightNumber + '\'' +
                ", departurePoint='" + departurePoint + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalPoint='" + arrivalPoint + '\'' +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
