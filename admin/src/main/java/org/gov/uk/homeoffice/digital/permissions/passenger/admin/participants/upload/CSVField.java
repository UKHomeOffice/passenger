package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

public enum  CSVField{
    ID("Unique ID No"),
    GWF("GWF ref"),
    VAF("VAF No"),
    CAS("CAS No"),
    FIRST_NAME("First Name"),
    MIDDLE_NAME("Middle Name"),
    SURNAME("Surname"),
    GENDER("gender"),
    DATE_OF_BIRTH("Date Of Birth"),
    NATIONALITY("Nationality"),
    PASSPORT("Passport No"),
    MOBILE("Mobile Number"),
    EMAIL("Email Address"),
    VALID_FROM("Valid from"),
    VALID_UNTIL("Valid Until"),
    SPX("SPX number"),
    CAT_D_ENDORSEMENT("Cat D Endorsement"),
    FLIGHT_NO("Flight No"),
    DEPARTURE_AIRPORT("Departure Airport"),
    DEPARTURE_DATETIME("Departure Date/Time"),
    ARRIVAL_AIRPORT("Arrival Airport"),
    ARRIVAL_DATETIME("Arrival Date/Time"),
    INSTITUTION("University/College name"),
    INSTITUTION_ADDRESS("ACL Address"),
    ACTION("action"),
    REASON("reason");


    public final String description;

    CSVField(String description) {
        this.description = description;
    }
}
