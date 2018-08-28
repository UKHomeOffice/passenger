package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoginAttempt  implements Serializable{

    public final String passportNumber;
    public final String ipAddress;
    public final LocalDateTime time;
    public final boolean success;

    public LoginAttempt(String passportNumber, String ipAddress, LocalDateTime time, boolean success) {
        this.passportNumber = passportNumber;
        this.ipAddress = ipAddress;
        this.time = time;
        this.success = success;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginAttempt that = (LoginAttempt) o;

        if (success != that.success) return false;
        if (passportNumber != null ? !passportNumber.equals(that.passportNumber) : that.passportNumber != null)
            return false;
        if (ipAddress != null ? !ipAddress.equals(that.ipAddress) : that.ipAddress != null) return false;
        return time != null ? time.equals(that.time) : that.time == null;
    }

    @Override
    public int hashCode() {
        int result = passportNumber != null ? passportNumber.hashCode() : 0;
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (success ? 1 : 0);
        return result;
    }


    @Override
    public String toString() {
        return "LoginAttempt{" +
                "passportNumber='" + passportNumber + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", time=" + time +
                ", success=" + success +
                '}';
    }
}
