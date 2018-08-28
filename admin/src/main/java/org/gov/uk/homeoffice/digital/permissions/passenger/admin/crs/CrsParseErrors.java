package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import java.io.Serializable;
import java.util.List;

public class CrsParseErrors implements Serializable {
    public final String crsRow;
    public final List<String> message;

    public CrsParseErrors(String crsRow, List<String> message) {
        this.crsRow = crsRow;
        this.message = message;
    }

    public String getCrsRow() {
        return crsRow;
    }

    public List<String> getMessage() {
        return message;
    }
}
