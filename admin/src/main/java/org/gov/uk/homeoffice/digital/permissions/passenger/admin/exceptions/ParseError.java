package org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions;

import java.io.Serializable;

public class ParseError implements Serializable {
    public final String csvRow;
    public final Exception exception;

    public ParseError(String csvRow, Exception e) {
        this.csvRow = csvRow;
        this.exception = e;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParseError that = (ParseError) o;

        if (csvRow != null ? !csvRow.equals(that.csvRow) : that.csvRow != null) return false;
        return exception != null ? exception.equals(that.exception) : that.exception == null;
    }

    @Override
    public int hashCode() {
        int result = csvRow != null ? csvRow.hashCode() : 0;
        result = 31 * result + (exception != null ? exception.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParseError{" +
                "csvRow='" + csvRow + '\'' +
                ", exception=" + exception +
                '}';
    }
}
