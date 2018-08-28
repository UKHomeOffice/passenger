package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.RuleType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
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
public class VisaRuleContentRowMapperTest {

    @InjectMocks
    private VisaRuleContentRowMapper underTest;

    @Test
    public void testMapper() throws SQLException {
        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getLong("id")).thenReturn(1009L);
        when(mockResultSet.getString("rule")).thenReturn("CODE_1");
        when(mockResultSet.getString("content")).thenReturn("CONTENT");
        when(mockResultSet.getBoolean("enabled")).thenReturn(Boolean.TRUE);
        when(mockResultSet.getString("rule_type")).thenReturn("POSITIVE");

        final VisaRuleContent result = underTest.map(mockResultSet, mockStatementContext);

        assertThat(result.getId(), is(1009L));
        assertThat(result.getRule(), is("CODE_1"));
        assertThat(result.getContent(), is("CONTENT"));
        assertThat(result.getEnabled(), is(Boolean.TRUE));
        assertThat(result.getRuleType(), is(RuleType.POSITIVE));
    }

}