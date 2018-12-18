package org.gov.uk.homeoffice.digital.permissions.passenger.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import static java.lang.String.format;

public class DateTimeUtils {

    private static final String DEFAULT_DATE_PATTERN = "dd MMM yyyy";
    private static final String DEFAULT_DATE_TIME_PATTERN = "dd MMM yyyy HH:mm";

    public static final String DISPLAY_DATE_TIME_PATTERN = "EEEE, d MMM yyyy";

    public static String parse(final LocalDate date) {
        return parse(date, DEFAULT_DATE_PATTERN);
    }

    public static String parse(final LocalDateTime dateTime) {
        return parse(dateTime, DEFAULT_DATE_TIME_PATTERN);
    }

    public static String parse(final LocalDate date, final String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String parse(final LocalDateTime dateTime, final String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String parse(final LocalDate date, final String pattern, boolean includeOrdinal) {
        return (includeOrdinal && pattern.contains("'%s'"))
                ? format(date.format(DateTimeFormatter.ofPattern(pattern)), getOrdinalFor(date.get(ChronoField.DAY_OF_MONTH)))
                : date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String parse(final LocalDateTime dateTime, final String pattern, boolean includeOrdinal) {
        return (includeOrdinal && pattern.contains("'%s'"))
                ? format(dateTime.format(DateTimeFormatter.ofPattern(pattern)), getOrdinalFor(dateTime.get(ChronoField.DAY_OF_MONTH)))
                : dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    private static String getOrdinalFor(int dayOfMonth) {
        if (dayOfMonth < 1 || dayOfMonth > 31)
            throw new IllegalArgumentException("Invalid day of month: " + dayOfMonth);
        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            return "th";
        }
        switch (dayOfMonth % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    public static String toDisplayDate(final LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.BASIC_ISO_DATE) : "";
    }

    public static LocalDate fromDisplayDate(final String displayDate){
        return LocalDate.parse(displayDate, DateTimeFormatter.BASIC_ISO_DATE);
    }

}
