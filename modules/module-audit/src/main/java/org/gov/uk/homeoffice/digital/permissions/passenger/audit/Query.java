package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

public class Query {

    static final String QUERY_INSERT_AUDIT = "INSERT INTO audit (user_name, content, result, date_time, passenger_email, passenger_passport_no, passenger_name) VALUES (:userName, :content, :result, :dateTime, :passengerEmail, :passengerPassportNumber, :passengerName)";

    static final String QUERY_COUNT = "SELECT COUNT (id) FROM audit";

    static final String QUERY_COUNT_IN_RANGE  = "SELECT COUNT(1) FROM audit WHERE date_time >= STR_TO_DATE(:fromDate,'%d%m%Y') and date_time < STR_TO_DATE(:toDate,'%d%m%Y')";

    static final String QUERY_SELECT_IN_RANGE = "SELECT * FROM audit WHERE date_time >= STR_TO_DATE(:fromDate,'%d%m%Y') and date_time < STR_TO_DATE(:toDate,'%d%m%Y') order by date_time";

    static final String QUERY_SELECT_IN_RANGE_FOR_USER = "SELECT * FROM audit WHERE date_time >= STR_TO_DATE(:fromDate,'%d%m%Y') and date_time < STR_TO_DATE(:toDate,'%d%m%Y') and user_name = :userName order by date_time";

    static final String QUERY_SELECT_IN_RANGE_FOR_TEAM = "SELECT * FROM audit WHERE date_time >= STR_TO_DATE(:fromDate,'%d%m%Y') and date_time < STR_TO_DATE(:toDate,'%d%m%Y') and team like :teamName order by date_time";

    static final String QUERY_SELECT_IN_RANGE_FOR_USER_AND_TEAM = "SELECT * FROM audit WHERE date_time >= STR_TO_DATE(:fromDate,'%d%m%Y') and date_time < STR_TO_DATE(:toDate,'%d%m%Y') and user_name = :userName and team like :teamName order by date_time";
}

