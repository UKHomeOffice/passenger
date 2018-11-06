package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;

import java.sql.ResultSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VisaCountByStatusMapperTest {

    @Test
    public void shouldMapVisaTypeCountStatus() throws Exception {
        final VisaCountByStatusMapper underTest = new VisaCountByStatusMapper();

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getString("status")).thenReturn(VisaStatus.ISSUED.name());
        when(mockResultSet.getInt(2)).thenReturn(1);

        final Tuple<VisaStatus, Integer> result = underTest.map(mockResultSet, mockStatementContext);

        assertThat(result.get_1(), is(VisaStatus.ISSUED));
        assertThat(result.get_2(), is(1));
    }

}