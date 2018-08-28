package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;

public interface Query {

    String INSERT_ENTRY_CLEARANCE = "INSERT INTO entry_clearance (" +
            "passport_number, " +
            "start_date," +
            "end_date, " +
            "visa_valid_to_date, " +
            "tier_type, " +
            "passport_nationality, " +
            "surname, other_names, " +
            "date_of_birth, " +
            "vaf_number, " +
            "cas_number, " +
            "spx_number, " +
            "conditions_1, " +
            "conditions_2" +
            ") VALUES (" +
            ":passportNumber," +
            ":startDate," +
            ":endDate," +
            ":visaValidToDate," +
            ":tierType," +
            ":passportNationality," +
            ":surname," +
            ":otherNames," +
            ":dateOfBirth," +
            ":vafNumber," +
            ":casNumber," +
            ":spxNumber," +
            ":conditions1," +
            ":conditions2)";

    String UPDATE_ENTRY_CLEARANCE = "UPDATE entry_clearance SET start_date = :startDate, end_date = :endDate, visa_valid_to_date = :visaValidToDate, tier_type = :tierType, passport_nationality = :passportNationality, surname = :surname, other_names = :otherNames, date_of_birth = :dateOfBirth, vaf_number = :vafNumber, cas_number = :casNumber, spx_number = :spxNumber, conditions_1 = :conditions1, conditions_2 = :conditions2 WHERE passport_number = :passportNumber";

    String SELECT_BY_PASSPORT_NUMBER = "SELECT * FROM entry_clearance WHERE passport_number = :passportNumber";

}
