package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

final class Query {

    static final String QUERY_INSERT_AUDIT = "INSERT INTO audit (user_name, content, result, date_time, passenger_email, passenger_passport_no, passenger_name) VALUES (:userName, :content, :result, :dateTime, :passengerEmail, :passengerPassportNumber, :passengerName)";

    static final String QUERY_COUNT = "SELECT COUNT (id) FROM audit";

    static final String SELECT_BY_PASSPORT_NUMBER = "SELECT * FROM audit WHERE passenger_passport_no = :passportNumber";

    static final String SELECT_BY_PASSENGER_EMAIL = "SELECT * FROM audit WHERE passenger_email = :emailAddress";

    static final String SELECT_BY_PASSENGER_NAME = "SELECT * FROM audit WHERE passenger_name LIKE :name";

    static final String SELECT_BY_ADMIN_EMAIL = "SELECT * FROM audit WHERE user_name = :emailAddress";

}

