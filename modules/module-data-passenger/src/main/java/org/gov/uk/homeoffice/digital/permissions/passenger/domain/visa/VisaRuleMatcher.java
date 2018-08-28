package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public interface VisaRuleMatcher {

    /*
     * Checks whether a visa rule can be matched against a passport number and date of birth.
     * Consumer method is executed when a match is not found passing a list of business rules.
     */
    boolean hasVisaRule(VisaRecord visaRecord, Consumer<List<String>> onFailureAction);

}
