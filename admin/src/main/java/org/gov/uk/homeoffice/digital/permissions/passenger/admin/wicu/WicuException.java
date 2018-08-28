package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

public class WicuException extends RuntimeException {

    public WicuException(String message) {
        super(message);
    }

    public WicuException(Throwable cause) {
        super(cause);
    }
}
