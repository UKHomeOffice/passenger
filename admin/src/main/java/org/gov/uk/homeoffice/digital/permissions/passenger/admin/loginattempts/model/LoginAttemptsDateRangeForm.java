package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class LoginAttemptsDateRangeForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

    public boolean isValid(){
        return from != null && to != null;
    }

}
