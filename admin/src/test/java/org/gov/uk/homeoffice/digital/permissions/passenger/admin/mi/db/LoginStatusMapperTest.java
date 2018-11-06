package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;

import java.sql.ResultSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginStatusMapperTest {

    @Test
    public void shouldMapLoginStatus() throws Exception {
        final LoginStatusMapper underTest = new LoginStatusMapper();

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getBoolean("success")).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(2)).thenReturn(1);

        final Tuple<Boolean, Integer> result = underTest.map(mockResultSet, mockStatementContext);

        assertThat(result.get_1(), is(Boolean.TRUE));
        assertThat(result.get_2(), is(1));
    }

}