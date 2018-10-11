package org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Audit implements Serializable {

    private final Long id;
    private final String userName;
    private final LocalDateTime dateTime;
    private final String result;
    private final String content;
    private final String passengerName;
    private final String passengerEmail;
    private final String passengerPassportNumber;

    public Audit(Long id, String userName, LocalDateTime dateTime, String result, String content,
                 String passengerName, String passengerEmail, String passengerPassportNumber) {
        this.id = id;
        this.userName = userName;
        this.dateTime = dateTime;
        this.result = result;
        this.content = content;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPassportNumber = passengerPassportNumber;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getResult() {
        return result;
    }

    public String getContent() {
        return content;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public String getPassengerPassportNumber() {
        return passengerPassportNumber;
    }

    public String[] toAuditArray() {
        return new String[]{
                String.valueOf(id),
                userName,
                content,
                result,
                dateTime == null ? null : dateTime.toString(),
                passengerName,
                passengerEmail,
                passengerPassportNumber
        };
    }
}
