package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

public interface Query {

    String INSERT_SUCCESSFUL_ATTEMPT = "INSERT INTO login_attempts (passport_number, ip_address, success) VALUES (:passportNumber, :ipAddress, true)";

    String INSERT_UNSUCCESSFUL_ATTEMPT = "INSERT INTO login_attempts (passport_number, ip_address, success) VALUES (:passportNumber, :ipAddress, false)";

    String SELECT_SINCE = "SELECT * FROM login_attempts WHERE created > :since AND passport_number = :passportNumber";

    String SELECT_BETWEEN = "SELECT * FROM login_attempts WHERE created >= :from AND created <= :to ORDER BY created DESC";
}
