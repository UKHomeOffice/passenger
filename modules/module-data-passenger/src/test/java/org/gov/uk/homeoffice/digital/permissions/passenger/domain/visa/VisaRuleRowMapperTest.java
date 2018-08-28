package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisaRuleRowMapperTest {

    @InjectMocks
    private VisaRuleRowMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getString("rule")).thenReturn("RULE_1");
        when(mockResultSet.getBoolean("enabled")).thenReturn(Boolean.TRUE);

        final VisaRule visaRule = underTest.map(mockResultSet, mockStatementContext);
        assertThat(visaRule.getRule(), is("RULE_1"));
        assertThat(visaRule.getEnabled(), is(Boolean.TRUE));
    }

}