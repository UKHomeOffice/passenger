package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CrsRecord implements Serializable {
    private Long id;
    private String gwfRef;
    private String vafNo;
    private String casNo;
    private String cosNo;
    private String postName;
    private String familyName;
    private String otherName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String mobileNumber;
    private String emailAddress;
    private String localAddress;
    private VisaStatus status;
    private String action;
    private String reason;
    private String ecType;
    private String entryType;
    private String visaEndorsement;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String sponsorName;
    private String sponsorType;
    private String sponsorAddress;
    private String sponsorSpxNo;
    private String additionalEndorsement1;
    private String additionalEndorsement2;
    private String catDEndors1;
    private String catDEndors2;
    private String uniCollegeName;
    private String brpCollectionInfo;
    private LocalDate expectedTravelDate;
    private LocalDateTime updated;
    private Set<String> emailsSent;
    private String updatedBy;
    private boolean isCreated;

    public boolean isInvalid() {
        return  emailAddress == null ||
                passportNumber == null ||
                dateOfBirth == null ||
                status == null ||
                nationality == null;
    }

    public String getFullName() {
        return (postName + " " + familyName).trim();
    }

}
