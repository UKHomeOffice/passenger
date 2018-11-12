package org.gov.uk.homeoffice.digital.permissions.passenger.issue.ui.model;

import lombok.Data;

@Data
public class TechnicalIssueForm {

    private String name;
    private String emailAddress;
    private String passportNumber;
    private String issueDetail;

}
