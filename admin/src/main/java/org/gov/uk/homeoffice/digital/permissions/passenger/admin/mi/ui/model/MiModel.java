package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.ui.model;

import lombok.Data;

@Data
public class MiModel {

    private int month;
    private int year;

    private int[] visaStatusData;
    private int[] loginData;

}
