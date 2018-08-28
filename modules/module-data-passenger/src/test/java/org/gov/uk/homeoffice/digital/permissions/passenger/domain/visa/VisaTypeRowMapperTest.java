package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
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
public class VisaTypeRowMapperTest {

    @InjectMocks
    private VisaTypeRowMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);
        final long instant = System.currentTimeMillis();

        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("name")).thenReturn("name");
        when(mockResultSet.getString("notes")).thenReturn("notes");
        when(mockResultSet.getBoolean("enabled")).thenReturn(Boolean.TRUE);
        when(mockResultSet.getTimestamp("created")).thenReturn(new Timestamp(instant));

        final VisaType visaType = underTest.map(mockResultSet, mockStatementContext);
        assertThat(visaType.getId(), is(1L));
        assertThat(visaType.getName(), is("name"));
        assertThat(visaType.getNotes(), is("notes"));
        assertThat(visaType.getEnabled(), is(Boolean.TRUE));
        assertThat(visaType.getCreated(), is(LocalDateTime.ofInstant(Instant.ofEpochMilli(instant), ZoneId.systemDefault())));
    }

}