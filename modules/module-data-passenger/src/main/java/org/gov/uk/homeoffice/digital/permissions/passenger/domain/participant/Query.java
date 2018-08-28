package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

public interface Query {

    String INSERT_PARTICIPANT = "INSERT INTO participant (" +
            "id, " +
            "surname, " +
            "date_of_birth, " +
            "passport_number, " +
            "mobile_number, " +
            "email, " +
            "acl_address, " +
            "cas_number, " +
            "first_name, " +
            "stage, " +
            "gwf_number, " +
            "vaf_number, " +
            "middle_name, " +
            "nationality, " +
            "emails_sent, " +
            "updated_by, " +
            "last_updated " +
            ") VALUES (" +
            ":id," +
            ":surName," +
            ":dateOfBirth," +
            ":passportNumber," +
            ":mobileNumber," +
            ":email," +
            ":institutionAddress," +
            ":cas," +
            ":firstName," +
            "NULL," +
            ":gwf," +
            ":vaf," +
            ":middleName," +
            ":nationality," +
            ":emailsSentString," +
            ":updatedBy," +
            "NOW()" +
            ")";

    String UPDATE_PARTICIPANT = "UPDATE participant SET surname = :surName, date_of_birth = :dateOfBirth, passport_number = :passportNumber, mobile_number = :mobileNumber, email = :email, acl_address = :institutionAddress, cas_number = :cas, first_name = :firstName, stage = NULL, gwf_number = :gwf, vaf_number = :vaf, middle_name = :middleName, nationality = :nationality, updated_by = :updatedBy, last_updated=NOW() WHERE id = :id \n";

    String SELECT_BY_ID = "SELECT * FROM participant WHERE id = :id";

    String SELECT_BY_ID_BY_ID = "SELECT id FROM participant WHERE id = :id";

    String SELECT_ALL = "SELECT * FROM participant ORDER BY id ASC";

    String UPDATE_EMAILS_SENT = "UPDATE participant SET emails_sent = :emailsSentString WHERE id = :id";

    String SELECT_BY_PASSPORT_NUMBER = "SELECT * FROM participant WHERE passport_number = :passportNumber";

    String SELECT_BY_PASSPORT_NUMBER_AND_DATE_OF_BIRTH = "SELECT * FROM participant WHERE passport_number = :passportNumber AND date_of_birth=:dateOfBirth";

    String SELECT_BY_VISA_VALID_FROM_RANGE = "SELECT p.* " +
            "FROM participant p " +
            "JOIN visa v " +
            "ON p.passport_number = v.passport_number " +
            "WHERE v.valid_from BETWEEN :lowerLimitIncluded AND :upperLimitIncluded";

    String DELETE_PARTICIPANT = "DELETE FROM participant WHERE passport_number = :passportNumber ";
}
