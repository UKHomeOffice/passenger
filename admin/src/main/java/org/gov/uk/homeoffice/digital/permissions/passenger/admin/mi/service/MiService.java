package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.time.Month;
import java.util.Collection;

public interface MiService {

    Collection<Tuple<VisaStatus, Integer>> visaCountByStatusForMonth(Month month, int year);

    Collection<Tuple<Boolean, Integer>> loginCountForMonth(Month month, int year);

}
