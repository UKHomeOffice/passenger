package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
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
public class ParticipantMapperTest {

    @InjectMocks
    private ParticipantMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final Timestamp ts = new Timestamp(Instant.now().getEpochSecond());
        final Date date = new Date(Instant.now().getEpochSecond());

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getLong("id")).thenReturn(900L);
        when(mockResultSet.getString("gwf_number")).thenReturn("gwf_number");
        when(mockResultSet.getString("vaf_number")).thenReturn("vaf_number");
        when(mockResultSet.getString("cas_number")).thenReturn("cas_number");
        when(mockResultSet.getString("first_name")).thenReturn("first_name");
        when(mockResultSet.getString("middle_name")).thenReturn("middle_name");
        when(mockResultSet.getString("surname")).thenReturn("surname");
        when(mockResultSet.getDate("date_of_birth")).thenReturn(date);
        when(mockResultSet.getString("nationality")).thenReturn("nationality");
        when(mockResultSet.getString("passport_number")).thenReturn("passport_number");
        when(mockResultSet.getString("mobile_number")).thenReturn("mobile_number");
        when(mockResultSet.getString("email")).thenReturn("email");
        when(mockResultSet.getString("acl_address")).thenReturn("acl_address");
        when(mockResultSet.getString("emails_sent")).thenReturn("emails_sent");
        when(mockResultSet.getString("updated_by")).thenReturn("updated_by");
        when(mockResultSet.getTimestamp("created")).thenReturn(ts);
        when(mockResultSet.getTimestamp("last_updated")).thenReturn(ts);

        final Participant result = underTest.map(mockResultSet, mockStatementContext);
        assertThat(result.getId(), is(900L));
        assertThat(result.getGwf(), is("gwf_number"));
        assertThat(result.getVaf(), is("vaf_number"));
        assertThat(result.getCas(), is("cas_number"));
        assertThat(result.getFirstName(), is("first_name"));
        assertThat(result.getMiddleName(), is("middle_name"));
        assertThat(result.getSurName(), is("surname"));
        assertThat(result.getDateOfBirth(), is(date.toLocalDate()));
        assertThat(result.getNationality(), is("nationality"));
        assertThat(result.getPassportNumber(), is("passport_number"));
        assertThat(result.getMobileNumber(), is("mobile_number"));
        assertThat(result.getEmail(), is("email"));
        assertThat(result.getInstitutionAddress(), is("acl_address"));
        assertThat(result.getEmailsSent().toArray()[0], is("emails_sent"));
        assertThat(result.getUpdatedBy(), is("updated_by"));
        assertThat(result.getCreated(), is(LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault())));
        assertThat(result.getUpdated(), is(LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault())));
    }


}