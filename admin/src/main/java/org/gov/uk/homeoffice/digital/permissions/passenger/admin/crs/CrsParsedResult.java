package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;


import lombok.Data;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.ParseError;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class CrsParsedResult implements Serializable {
    private  List<CrsRecord> crsRecords;
    private  List<CrsRecord> updatedCrsRecords;
    private  List<CrsParseErrors> parseErrors;

    public CrsParsedResult(List<CrsRecord> crsRecords, List<CrsParseErrors> parseErrors) {
        this.crsRecords = crsRecords;
        this.updatedCrsRecords = Collections.emptyList();
        this.parseErrors = parseErrors;
    }

    public CrsParsedResult withParticipants(List<CrsRecord> crsRecords) {
        return new CrsParsedResult(crsRecords, parseErrors);
    }

    public CrsParsedResult withParseErrors( List<CrsParseErrors> parseErrors) {
        return new CrsParsedResult(crsRecords, parseErrors);
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
