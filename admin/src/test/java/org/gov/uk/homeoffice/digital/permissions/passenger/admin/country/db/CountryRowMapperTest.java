package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CountryRowMapperTest {

    @Test
    public void shouldMapCountryRow() throws Exception {
        final CountryRowMapper underTest = new CountryRowMapper();

        final Timestamp created = new Timestamp(System.currentTimeMillis());
        final Timestamp updated = new Timestamp(System.currentTimeMillis());

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getString("iso_country_code")).thenReturn(Locale.UK.getCountry());
        when(mockResultSet.getBoolean("enabled")).thenReturn(Boolean.TRUE);
        when(mockResultSet.getTimestamp("created")).thenReturn(created);
        when(mockResultSet.getTimestamp("updated")).thenReturn(updated);

        final Country country = underTest.map(mockResultSet, mockStatementContext);

        assertThat(country.getCountry().getCountry(), is("GB"));
        assertThat(country.getId(), is("GB"));
        assertThat(country.getEnabled(), is(true));
        assertThat(country.getCreated(), is(created.toLocalDateTime()));
        assertThat(country.getUpdated(), is(updated.toLocalDateTime()));
    }

}