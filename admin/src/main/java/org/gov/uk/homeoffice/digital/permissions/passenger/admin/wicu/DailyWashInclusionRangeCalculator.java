package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DailyWashInclusionRangeCalculator {
    private static final int DAYS_BEFORE = 2;
    private static final int VALIDITY_DAYS = 30;

    public Tuple<LocalDate, LocalDate> calculate(LocalDate today) {
        LocalDate upperLimit = today.plusDays(DAYS_BEFORE);
        LocalDate lowerLimit = today.minusDays(VALIDITY_DAYS);

        return Tuple.tpl(lowerLimit, upperLimit);
    }
}
