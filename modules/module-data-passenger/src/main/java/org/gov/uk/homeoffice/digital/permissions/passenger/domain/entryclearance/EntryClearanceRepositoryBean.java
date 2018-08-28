package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action.SaveOrUpdateAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action.SelectByPassportNumberAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action.SelectByPassportNumberAndDOBAction;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class EntryClearanceRepositoryBean implements EntryClearanceRepository {

    private final Jdbi jdbi;

    public EntryClearanceRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void createOrUpdate(EntryClearance ec) {
        jdbi.useTransaction(new SaveOrUpdateAction(ec));
    }

    @Override
    public Optional<EntryClearance> getEntryClearanceByPassportNumber(String passportNumber) {
        return jdbi.withHandle(new SelectByPassportNumberAction(passportNumber));
    }

    @Override
    public Optional<EntryClearance> getByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth) {
        return jdbi.withHandle(new SelectByPassportNumberAndDOBAction(passportNumber, dateOfBirth));
    }

}
