package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

public enum CrsField {
    GWF_REF("GWF Ref"),
    VAF_NUMBER("VAF No"),
    CAS_NUMBER("CAS No"),
    COS_NUMBER("COS No"),
    POST_NAME("Post name"),
    FAMILY_NAME("Family name"),
    OTHER_NAME("Other names"),
    GENDER("Gender"),
    DATE_OF_BIRTH("Date of Birth"),
    NATIONALITY("Nationality"),
    PASSPORT_NUMBER("Passport No"),
    MOBILE_NUMBER("Mobile No"),
    EMAIL_ADDRESS("Email address"),
    LOCAL_ADDRESS("Local address"),
    STATUS_DETAILS("Status details"),
    EC_TYPE("EC Type"),
    ENTRY_TYPE("Entry type"),
    VISA_ENDORSEMENT("Visa Endorsement"),
    VISA_VALID_FROM("Visa valid from"),
    VISA_VALID_TO("Visa valid to"),
    SPONSOR_DETAILS_NAME("Sponsor details: Name"),
    SPONSOR_DETAILS_TYPE("Sponsor details: Type"),
    SPONSOR_DETAILS_ADDRESS("Sponsor details: Address"),
    SPONSOR_DETAILS_SPX_NUMBER("Sponsor details: SPX no"),
    ADDITIONAL_ENDORSEMENT_1("Additional endorsement 1"),
    ADDITIONAL_ENDORSEMENT_2("Additional endorsement 2"),
    CAT_D_ENDORSEMENTS_1("Cat D endorsement 1"),
    CAT_D_ENDORSEMENT_2("Cat D endorsement 2"),
    UNIVERSITY_COLLEGE_NAME("University/College name"),
    BRP_COLLECTION_INFORMATION("BRP Collection information"),
    EXPECTED_TRAVEL_DATE("Expected travel date"),
    ACTION("action"),
    REASON("reason");

    String description;

    CrsField(String description) {
        this.description = description;
    }
}
