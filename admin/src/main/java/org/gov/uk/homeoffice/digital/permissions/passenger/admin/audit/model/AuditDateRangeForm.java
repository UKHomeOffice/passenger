package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class AuditDateRangeForm {

    @NotNull(message = "From date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @NotNull(message = "To date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

}
