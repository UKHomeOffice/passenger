package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import com.google.common.collect.Range;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.time.LocalDate;

import static java.time.Month.APRIL;
import static org.hamcrest.MatcherAssert.assertThat;

public class DailyWashInclusionRangeCalculatorTest {

    // a visa holder should be on WICU daily wash
    // from 2 days before visa.validFrom to 30 days after visa.validFrom
    //                ↓validFrom                       ↓validFrom+30
    // visa:  --------[--------------------------------)-------->
    // today: nnnnnnyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyynnnnnnnn

    private final LocalDate validFrom = LocalDate.of(2018, APRIL, 1);

    DailyWashInclusionRangeCalculator calculator = new DailyWashInclusionRangeCalculator();

    @Test
    public void daysBefore2DaysBeforeValidFromAreNotIncluded() {
        LocalDate today = validFrom.minusDays(3);
        assertThat(toRange(calculator.calculate(today)).contains(validFrom), Matchers.is(false));
    }

    @Test
    public void daysBetween2DaysInclusiveBeforeValidFromAndAfter30DaysInclusiveAreIncluded() {
        for (LocalDate today = validFrom.minusDays(2); today.isBefore(validFrom.plusDays(30 + 1)); today = today.plusDays(1)) {
            assertThat(today.toString(), toRange(calculator.calculate(today)).contains(validFrom), Matchers.is(true));
        }
    }

    @Test
    public void daysAfter30DaysAfterValidFromAreNotIncluded() {
        LocalDate today = validFrom.plusDays(32);
        assertThat(toRange(calculator.calculate(today)).contains(validFrom), Matchers.is(false));
    }

    private Range<LocalDate> toRange(Tuple<LocalDate, LocalDate> interval) {
        // this method mimics the sql '... where valid_from between x and y'
        return Range.closed(interval.get_1(), interval.get_2());
    }
}