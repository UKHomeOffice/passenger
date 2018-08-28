package org.gov.uk.homeoffice.digital.permissions.passenger;


import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// the script in 'scripts=...' needs to be executed as single statement
// but it contains ';' and the @Sql processing splits the script into multiple statements on ';'s
// this is a workaround to keep the entire as a single statement
@SqlConfig(separator = "@@")
@Sql(executionPhase=BEFORE_TEST_METHOD, scripts="classpath:/truncate_tables.sql")
public @interface TruncateTablesBeforeEachTest {
}
