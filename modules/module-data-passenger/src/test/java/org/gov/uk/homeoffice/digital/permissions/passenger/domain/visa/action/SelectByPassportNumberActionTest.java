package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectByPassportNumberActionTest {

    @Test
    public void shouldSelectByPassportNumber() {
        final String passportNumber = "passportNumber";
        final Visa visa = validVisa(passportNumber);

        final Handle mockHandle = mock(Handle.class);
        final VisaDAO mockDAO = mock(VisaDAO.class);

        when(mockHandle.attach(VisaDAO.class)).thenReturn(mockDAO);
        when(mockDAO.getByPassportNumber(visa.getPassportNumber())).thenReturn(visa);
        when(mockDAO.getEndorsementsForVisa(visa.getId())).thenReturn(Collections.singletonList("catEndorsements"));

        final SelectByPassportNumberAction underTest = new SelectByPassportNumberAction(passportNumber);
        final Optional<Visa> visaResult = underTest.withHandle(mockHandle);

        assertTrue(visaResult.isPresent());
        assertThat(visaResult.get(), is(visa));
    }

    private Visa validVisa(final String passportNumber) {
        return new Visa(
                1016L,
                passportNumber,
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 6,1),
                "spx",
                Collections.singletonList("catEndorsements"),
                VisaStatus.VALID,
                "reason");
    }


}