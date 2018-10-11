package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.Validate.notNull;


@Data
@Builder
@AllArgsConstructor
public class Participant implements Serializable {

    private long id;

    private String gwf;
    private String vaf;
    private String cas;
    private String cos;
    private String firstName;
    private String middleName;
    private String surName;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String mobileNumber;
    private String email;
    private String institutionAddress;
    private Set<String> emailsSent;
    private String emailsSentString;
    private String updatedBy;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Participant(Long id, String gwf, String vaf, String cas, String cos, String firstName, String middleName, String surName, LocalDate dateOfBirth,
                       String nationality, String passportNumber, String mobileNumber, String email, String institutionAddress,
                       Set<String> emailsSent, String updatedBy, LocalDateTime created, LocalDateTime updated) {
        this.id = notNull(id, "id must be provided");
        this.gwf =  gwf;
        this.vaf = vaf;
        this.cas = cas;
        this.cos = cos;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surName = notNull(surName, "surname must be provided");
        this.dateOfBirth = notNull(dateOfBirth, "date of birth must be provided");
        this.nationality = nationality;
        this.passportNumber = notNull(passportNumber,"passport number must be provided");
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.institutionAddress = institutionAddress;
        this.emailsSent = emailsSent;
        this.emailsSentString = ofNullable(emailsSent).map(val -> !val.isEmpty() ? String.join(",", val) : null).orElse(null);
        this.updatedBy = updatedBy;
        this.created = created;
        this.updated = updated;
    }

}
