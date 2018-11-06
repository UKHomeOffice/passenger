package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db;

final class MiSQL {

    static final String SELECT_VISA_COUNT_BY_STATUS = "SELECT status, COUNT(1) FROM crs_record " +
            "WHERE updated BETWEEN :from AND :to " +
            "GROUP BY status";

    static final String SELECT_USER_LOGIN_COUNT = "SELECT success, COUNT(1) FROM login_attempts " +
            "WHERE created BETWEEN :from AND :to " +
            "GROUP BY success";

}
