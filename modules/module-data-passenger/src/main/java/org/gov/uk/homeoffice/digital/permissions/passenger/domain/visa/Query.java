package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

public interface Query {

    static final String INSERT_VISA = "INSERT INTO visa (passport_number, valid_from, valid_to, spx_number, status, reason) VALUES (:passportNumber,:validFrom,:validTo,:spx, :status, :reason)";
    static final String UPDATE_VISA = "UPDATE visa SET  passport_number = :passportNumber, valid_from = :validFrom, valid_to = :validTo, spx_number = :spx, status=:status, reason=:reason WHERE id = :id ";
    static final String SELECT_BY_PASSPORT_NUMBER = "SELECT * FROM visa WHERE passport_number = :passportNumber";

    static final String INSERT_ENDORSEMENT = "INSERT INTO endorsements (visa_id, value) VALUES (:visaId,:value)";

    static final String SELECT_ENDORSEMENTS_FOR_VISA = "SELECT value FROM endorsements WHERE visa_id = :visaId";
    static final String DELETE_ENDORSEMENTS_FOR_VISA = "DELETE FROM endorsements WHERE visa_id = :visaId";

    static final String SELECT_ID_FOR_PASSPORT_NUMBER = "SELECT id FROM visa WHERE passport_number = :passportNumber";

}
