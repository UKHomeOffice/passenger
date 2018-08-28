package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.annotation;

public enum AuditAction {

    CREATE(true, "create"), VIEW(true, "view"), UPDATE(true, "update"), DELETE(true, "delete"), DOWNLOAD(true, "download"), UPLOAD(true, "upload");

    private Boolean isAnActionOnResource; // True : store resourceId false: store params
    private String description;

    AuditAction(Boolean isResourceAction, String description) {
        this.isAnActionOnResource = isResourceAction;
        this.description = description;
    }

    public Boolean isAnActionOnResource() {
        return isAnActionOnResource;
    }

    public String getDescription() {
        return description;
    }
}
