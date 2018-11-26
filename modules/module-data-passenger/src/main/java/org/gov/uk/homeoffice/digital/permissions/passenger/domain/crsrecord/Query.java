package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

public interface Query {

    String UPDATE_CRS_RECORD = "UPDATE crs_record SET " +
            "gwf_ref = :gwfRef, " +
            "vaf_no = :vafNo, " +
            "cas_no = :casNo, " +
            "cos_no = :cosNo, " +
            "post_name = :postName, " +
            "family_name = :familyName, " +
            "other_name = :otherName, " +
            "gender = :gender, " +
            "date_of_birth = :dateOfBirth, " +
            "nationality = :nationality, " +
            "passport_no = :passportNumber, " +
            "mobile_no = :mobileNumber, " +
            "email_address = :emailAddress, " +
            "local_address = :localAddress, " +
            "status = :status, " +
            "action = :action, " +
            "reason = :reason, " +
            "ec_type = :ecType, " +
            "entry_type = :entryType, " +
            "visa_endorsement = :visaEndorsement, " +
            "valid_from = :validFrom, " +
            "valid_to = :validTo, " +
            "sponsor_name = :sponsorName, " +
            "sponsor_type = :sponsorType, " +
            "sponsor_address = :sponsorAddress, " +
            "sponsor_spx_no = :sponsorSpxNo, " +
            "add_endors_1 = :additionalEndorsement1, " +
            "add_endors_2 = :additionalEndorsement2, " +
            "cat_d_endors_1 = :catDEndors1, " +
            "cat_d_endors_2 = :catDEndors2, " +
            "uni_college_name = :uniCollegeName, " +
            "brp_collection_info = :brpCollectionInfo, " +
            "emails_sent = :emailsSent, " +
            "expected_travel_date = :expectedTravelDate, " +
            "updated = :updated, " +
            "updated_by = :updatedBy " +
            "WHERE id = :id";

    String INSERT_CRS_RECORD = "INSERT INTO crs_record (" +
            "gwf_ref, " +
            "vaf_no, " +
            "cas_no, " +
            "cos_no, " +
            "post_name, " +
            "family_name, " +
            "other_name, " +
            "gender, " +
            "date_of_birth, " +
            "nationality, " +
            "passport_no, " +
            "mobile_no, " +
            "email_address, " +
            "local_address, " +
            "status, " +
            "action, " +
            "reason, " +
            "ec_type, " +
            "entry_type, " +
            "visa_endorsement, " +
            "valid_from, " +
            "valid_to, " +
            "sponsor_name, " +
            "sponsor_type, " +
            "sponsor_address, " +
            "sponsor_spx_no, " +
            "add_endors_1, " +
            "add_endors_2, " +
            "cat_d_endors_1, " +
            "cat_d_endors_2, " +
            "uni_college_name, " +
            "brp_collection_info, " +
            "expected_travel_date," +
            "emails_sent," +
            "updated," +
            "updated_by" +
            ") VALUES (" +
            ":gwfRef," +
            ":vafNo," +
            ":casNo," +
            ":cosNo," +
            ":postName," +
            ":familyName," +
            ":otherName," +
            ":gender," +
            ":dateOfBirth," +
            ":nationality," +
            ":passportNumber," +
            ":mobileNumber," +
            ":emailAddress," +
            ":localAddress," +
            ":status," +
            ":action," +
            ":reason," +
            ":ecType," +
            ":entryType," +
            ":visaEndorsement," +
            ":validFrom," +
            ":validTo," +
            ":sponsorName," +
            ":sponsorType," +
            ":sponsorAddress," +
            ":sponsorSpxNo," +
            ":additionalEndorsement1," +
            ":additionalEndorsement2," +
            ":catDEndors1," +
            ":catDEndors2," +
            ":uniCollegeName," +
            ":brpCollectionInfo," +
            ":expectedTravelDate," +
            ":emailsSent," +
            ":updated," +
            ":updatedBy" +
            ")";


    String SELECT_BY_ID = "SELECT * FROM crs_record WHERE id = :id";

    String SELECT_ID_BY_ID = "SELECT id FROM crs_record WHERE id = :id";

    String SELECT_BY_PASSPORT_NUMBER_AND_DATE_OF_BIRTH = "SELECT * FROM crs_record WHERE passport_no = :passportNumber AND date_of_birth=:dateOfBirth";

    String SELECT_BY_PASSPORT_NUMBER = "SELECT * FROM crs_record WHERE passport_no = :passportNumber";

    String SELECT_ALL = "SELECT * FROM crs_record ORDER BY id DESC";

    String DELETE_CRS_RECORD = "DELETE FROM crs_record WHERE id = :id";

    String DELETE_CRS_RECORD_OLDER_THAN = "DELETE FROM crs_record WHERE valid_from < :dateTime";

    String SELECT_VALID_WITHIN_RANGE = "SELECT * FROM crs_record WHERE valid_from BETWEEN :lowerLimitIncluded AND :upperLimitIncluded";

}
