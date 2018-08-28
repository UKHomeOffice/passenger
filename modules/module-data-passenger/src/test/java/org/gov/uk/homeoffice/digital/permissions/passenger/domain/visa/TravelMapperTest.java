package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Travel;
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
public class TravelMapperTest {

    @InjectMocks
    private TravelMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final Timestamp ts = new Timestamp(Instant.now().getEpochSecond());

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getString("flightnumber")).thenReturn("flightnumber");
        when(mockResultSet.getString("departure_point")).thenReturn("departure_point");
        when(mockResultSet.getTimestamp("departure_time")).thenReturn(ts);
        when(mockResultSet.getString("arrival_point")).thenReturn("arrival_point");
        when(mockResultSet.getTimestamp("arrival_time")).thenReturn(ts);

        final Travel travel = underTest.map(mockResultSet, mockStatementContext);
        assertThat(travel.flightNumber, is("flightnumber"));
        assertThat(travel.departurePoint, is("departure_point"));
        assertThat(travel.departureTime, is(LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault())));
        assertThat(travel.arrivalPoint, is("arrival_point"));
        assertThat(travel.arrivalTime, is(LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault())));
    }

}