package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

final class VisaTypeSQL {

    static final String INSERT = "INSERT INTO visa_type (name, notes, enabled, created) VALUES (:name, :notes, :enabled, NOW())";
    static final String UPDATE = "UPDATE visa_type SET name = :name, notes = :notes, enabled = :enabled WHERE id = :id";
    static final String SELECT_BY_ID = "SELECT * FROM visa_type WHERE id = :id";
    static final String SELECT_BY_NAME = "SELECT * FROM visa_type WHERE name = :name LIMIT 1";
    static final String DELETE = "DELETE FROM visa_type WHERE id = :id";
    static final String SELECT_ALL = "SELECT * FROM visa_type";

}
