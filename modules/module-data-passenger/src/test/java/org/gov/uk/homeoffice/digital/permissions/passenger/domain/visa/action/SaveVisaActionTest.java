package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class SaveVisaActionTest {

    @Test
    public void shouldUpdateVisa() {
        final String passportNumber = "123ABC456D";
        final Long visaId = 1016L;
        final Visa visa = validVisa(visaId, passportNumber);

        final Handle mockHandle = mock(Handle.class);
        final VisaDAO mockDAO = mock(VisaDAO.class);

        when(mockHandle.attach(VisaDAO.class)).thenReturn(mockDAO);
        when(mockDAO.exists(passportNumber)).thenReturn(visaId);

        final SaveVisaAction underTest = new SaveVisaAction(visa);
        underTest.useHandle(mockHandle);

        verify(mockDAO).update(visa);
        verify(mockDAO).deleteEndorsementsForVisa(visaId);
        verify(mockDAO).saveEndorsement("catEndorsements", visaId);
    }

    @Test
    public void shouldSaveVisa() {
        final String passportNumber = "123ABC456D";
        final Long visaId = 1016L;
        final Visa visa = validVisa(visaId, passportNumber);

        final Handle mockHandle = mock(Handle.class);
        final VisaDAO mockDAO = mock(VisaDAO.class);

        when(mockHandle.attach(VisaDAO.class)).thenReturn(mockDAO);
        when(mockDAO.exists(passportNumber)).thenReturn(null);
        when(mockDAO.save(visa)).thenReturn(visaId);

        final SaveVisaAction underTest = new SaveVisaAction(visa);
        underTest.useHandle(mockHandle);

        verify(mockDAO).save(visa);
        verify(mockDAO).saveEndorsement("catEndorsements", visaId);
    }

    private Visa validVisa(final Long id, final String passportNumber) {
        return new Visa(
                id,
                passportNumber,
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 6,1),
                "spx",
                Collections.singletonList("catEndorsements"),
                VisaStatus.VALID,
                "reason");
    }

}