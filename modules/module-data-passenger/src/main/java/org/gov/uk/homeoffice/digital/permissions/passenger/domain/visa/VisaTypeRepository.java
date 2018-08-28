package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;

import java.util.Collection;
import java.util.Optional;

public interface VisaTypeRepository {

    VisaType save(VisaType visaType);

    void remove(Long id);

    Optional<VisaType> findOneById(Long id);

    Collection<VisaType> findAll();

}
