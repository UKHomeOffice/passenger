package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MiRepository {

    Collection<Tuple<VisaStatus, Integer>> countVisaByStatus(LocalDateTime from,
                                                             LocalDateTime to);

    Collection<Tuple<Boolean, Integer>> countSuccessfulLogin(LocalDateTime from,
                                                             LocalDateTime to);

}
