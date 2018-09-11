package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import lombok.Value;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Value
public class CrsParsedResult implements Serializable {

    private List<CrsRecord> crsRecords;
    private List<CrsRecord> updatedCrsRecords;
    private List<CrsParseErrors> parseErrors;

    public CrsParsedResult(List<CrsRecord> crsRecords, List<CrsParseErrors> parseErrors) {
        this(crsRecords, null, parseErrors);
    }

    private CrsParsedResult(final List<CrsRecord> crsRecords,
                            final List<CrsRecord> updatedCrsRecords,
                            final List<CrsParseErrors> parseErrors) {
        this.crsRecords = (crsRecords == null) ? Collections.emptyList() : crsRecords;
        this.updatedCrsRecords = (updatedCrsRecords == null) ? Collections.emptyList() : updatedCrsRecords;
        this.parseErrors = (parseErrors == null) ? Collections.emptyList() : parseErrors;
    }

    public CrsParsedResult withParticipants(List<CrsRecord> crsRecords) {
        return new CrsParsedResult(crsRecords, parseErrors);
    }

    public CrsParsedResult withParseErrors(List<CrsParseErrors> parseErrors) {
        return new CrsParsedResult(crsRecords, parseErrors);
    }

    public CrsParsedResult withUpdatedCrsRecords(List<CrsRecord> updatedCrsRecords) {
        return new CrsParsedResult(crsRecords, updatedCrsRecords, parseErrors);
    }

    public long getNumberOfSuccessfullyUpdatedRecords() {
        return updatedCrsRecords.stream().filter(r -> !r.isCreated()).count();
    }

    public long getNumberOfSuccessfullyCreatedRecords() {
        return updatedCrsRecords.size() - getNumberOfSuccessfullyUpdatedRecords();
    }

    public long getNumberOfRecordsInError() {
        return parseErrors.size();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrsParsedResult that = (CrsParsedResult) o;

        if (crsRecords != null ? !crsRecords.equals(that.crsRecords) : that.crsRecords != null)
            return false;
        return parseErrors != null ? parseErrors.equals(that.parseErrors) : that.parseErrors == null;
    }

    @Override
    public int hashCode() {
        int result = crsRecords != null ? crsRecords.hashCode() : 0;
        result = 31 * result + (parseErrors != null ? parseErrors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParsedResult{" +
                "participant=" + crsRecords +
                ", parseErrors=" + parseErrors +
                '}';
    }
}
