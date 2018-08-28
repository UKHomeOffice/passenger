package org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.model;

import lombok.Data;

@Data
public class Passenger {

    private String passportNumber;
    private String day;
    private String month;
    private String year;

}
