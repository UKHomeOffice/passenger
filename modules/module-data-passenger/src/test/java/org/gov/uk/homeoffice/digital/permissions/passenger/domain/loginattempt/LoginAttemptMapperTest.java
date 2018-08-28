package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptMapperTest {

    @InjectMocks
    private LoginAttemptMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final Timestamp ts = new Timestamp(Instant.now().getEpochSecond());

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getString("passport_number")).thenReturn("passport_number");
        when(mockResultSet.getString("ip_address")).thenReturn("ip_address");
        when(mockResultSet.getTimestamp("created")).thenReturn(ts);
        when(mockResultSet.getBoolean("success")).thenReturn(Boolean.TRUE);

        final LoginAttempt result = underTest.map(mockResultSet, mockStatementContext);
        assertThat(result.passportNumber, is("passport_number"));
        assertThat(result.ipAddress, is("ip_address"));
        assertThat(result.time, is(LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault())));
        assertThat(result.success, is(Boolean.TRUE));
    }

}