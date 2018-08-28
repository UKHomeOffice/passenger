package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.EntryClearanceDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.*;

public class SaveOrUpdateActionTest {

    @Test
    public void shouldUpdateEntryClearance() {
        final EntryClearance entryClearance = entryClearance();

        final Handle mockHandle = mock(Handle.class);
        final EntryClearanceDAO mockDAO = mock(EntryClearanceDAO.class);

        when(mockHandle.attach(EntryClearanceDAO.class)).thenReturn(mockDAO);
        when(mockDAO.getEntryClearanceByPassportNumber(entryClearance.getPassportNumber())).thenReturn(entryClearance);

        final SaveOrUpdateAction underTest = new SaveOrUpdateAction(entryClearance);
        underTest.useHandle(mockHandle);

        verify(mockHandle).attach(EntryClearanceDAO.class);
        verify(mockDAO).update(entryClearance);
    }

    @Test
    public void shouldSaveEntryClearance() {
        final EntryClearance entryClearance = entryClearance();

        final Handle mockHandle = mock(Handle.class);
        final EntryClearanceDAO mockDAO = mock(EntryClearanceDAO.class);

        when(mockHandle.attach(EntryClearanceDAO.class)).thenReturn(mockDAO);
        when(mockDAO.getEntryClearanceByPassportNumber(entryClearance.getPassportNumber())).thenReturn(null);

        final SaveOrUpdateAction underTest = new SaveOrUpdateAction(entryClearance);
        underTest.useHandle(mockHandle);

        verify(mockHandle).attach(EntryClearanceDAO.class);
        verify(mockDAO).save(entryClearance);
    }

    private EntryClearance entryClearance() {
        return new EntryClearance(
                "passportNumber", LocalDate.now(), LocalDate.MAX, "passportNationality",
                "surname", "otherNames", "tierType", LocalDate.of(1980, 1, 1),
                "vafNumber", "casNumber", "spxNumber", LocalDate.now().plus(30, ChronoUnit.DAYS),
                "conditionsLine1", "conditionsLine2");
    }

}