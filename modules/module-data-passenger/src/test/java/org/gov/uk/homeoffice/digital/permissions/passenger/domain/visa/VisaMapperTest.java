package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisaMapperTest {

    @InjectMocks
    private VisaMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final Date date = new Date(Instant.now().getEpochSecond());

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getLong("id")).thenReturn(101L);
        when(mockResultSet.getString("passport_number")).thenReturn("passport_number");
        when(mockResultSet.getDate("valid_from")).thenReturn(date);
        when(mockResultSet.getDate("valid_to")).thenReturn(date);
        when(mockResultSet.getString("spx_number")).thenReturn("spx_number");
        when(mockResultSet.getString("status")).thenReturn(VisaStatus.VALID.name());
        when(mockResultSet.getString("reason")).thenReturn("reason");

        final Visa visa = underTest.map(mockResultSet, mockStatementContext);
        assertThat(visa.getId(), is(101L));
        assertThat(visa.getPassportNumber(), is("passport_number"));
        assertThat(visa.getValidFrom(), is(date.toLocalDate()));
        assertThat(visa.getValidTo(), is(date.toLocalDate()));
        assertThat(visa.getSpx(), is("spx_number"));
        assertThat(visa.getStatus(), is(VisaStatus.VALID));
        assertThat(visa.getReason(), is("reason"));
    }


}