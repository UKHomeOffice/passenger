package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.SaveVisaAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.SelectByPassportNumberAction;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VisaRepositoryBean implements VisaRepository {

    final Jdbi jdbi;

    public VisaRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void save(final Visa visa) {
        jdbi.useTransaction(new SaveVisaAction(visa));
    }

    public Optional<Visa> getByPassportNumber(final String passportNumber) {
        return jdbi.withHandle(new SelectByPassportNumberAction(passportNumber));
    }

}
