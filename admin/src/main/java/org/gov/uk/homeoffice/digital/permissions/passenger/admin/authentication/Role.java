package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

public enum Role {
    ROLE_ADMIN("Admin"), ROLE_WICU("WICU User"), ROLE_AUDIT("Audit");

    private String name;
    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
