package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaTypeRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.util.List;
import java.util.Optional;

public interface VisaTypeService {

    Tuple<Optional<VisaTypeRule>, List<String>> findVisaTypeRule(VisaRecord visaRecord);

}
