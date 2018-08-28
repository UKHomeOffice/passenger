package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class SelectVisaByValidFromActionTest {

    @Test
    public void shouldSelectVisaByValidFrom() {
        final LocalDate lowerLimitIncluded = LocalDate.now();
        final LocalDate upperLimitIncluded = LocalDate.now().plusDays(1);

        final Handle mockHandle = mock(Handle.class);
        final ParticipantDAO mockDAO = mock(ParticipantDAO.class);

        when(mockHandle.attach(ParticipantDAO.class)).thenReturn(mockDAO);

        final SelectVisaByValidFromAction underTest = new SelectVisaByValidFromAction(lowerLimitIncluded, upperLimitIncluded);
        underTest.withHandle(mockHandle);

        verify(mockDAO).getByVisaValidFrom(lowerLimitIncluded, upperLimitIncluded);
    }

}