package org.gov.uk.homeoffice.digital.permissions.passenger.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateTimeUtilsTest {

    @Test
    public void shouldParseLocalDateWithDefaultFormat() {
        assertThat(parse(LocalDate.of(1980, 10, 15)), is("15 Oct 1980"));
    }

    @Test
    public void shouldParseLocalDateTimeWithDefaultFormat() {
        assertThat(parse(LocalDateTime.of(1980, 10, 15, 20, 15)), is("15 Oct 1980 20:15"));
    }

    @Test
    public void shouldParseLocalDateWithCustomFormat() {
        assertThat(parse(LocalDate.of(1980, 10, 15), "dd MM yy"), is("15 10 80"));
    }

    @Test
    public void shouldParseLocalDateWithOrdinalFormat() {
        assertThat(parse(LocalDate.of(1980, 10, 1), "d'%s' MMM yyyy", true), is("1st Oct 1980"));
        assertThat(parse(LocalDate.of(1980, 10, 2), "d'%s' MMM yyyy", true), is("2nd Oct 1980"));
        assertThat(parse(LocalDate.of(1980, 10, 3), "d'%s' MMM yyyy", true), is("3rd Oct 1980"));
        assertThat(parse(LocalDate.of(1980, 10, 4), "d'%s' MMM yyyy", true), is("4th Oct 1980"));
        assertThat(parse(LocalDate.of(1980, 10, 21), "d'%s' MMM yyyy", true), is("21st Oct 1980"));
        assertThat(parse(LocalDate.of(1980, 10, 22), "d'%s' MMM yyyy", true), is("22nd Oct 1980"));
        assertThat(parse(LocalDate.of(1980, 10, 23), "d'%s' MMM yyyy", true), is("23rd Oct 1980"));
    }

    @Test
    public void shouldParseLocalDateTimeWithOrdinalFormat() {
        assertThat(parse(LocalDateTime.of(1980, 10, 1, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("1st Oct 1980 11:30"));
        assertThat(parse(LocalDateTime.of(1980, 10, 2, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("2nd Oct 1980 11:30"));
        assertThat(parse(LocalDateTime.of(1980, 10, 3, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("3rd Oct 1980 11:30"));
        assertThat(parse(LocalDateTime.of(1980, 10, 4, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("4th Oct 1980 11:30"));
        assertThat(parse(LocalDateTime.of(1980, 10, 21, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("21st Oct 1980 11:30"));
        assertThat(parse(LocalDateTime.of(1980, 10, 22, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("22nd Oct 1980 11:30"));
        assertThat(parse(LocalDateTime.of(1980, 10, 23, 11, 30), "d'%s' MMM yyyy HH:mm", true), is("23rd Oct 1980 11:30"));
    }

}