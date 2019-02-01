package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.*;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class VisaTypeRepositoryBean implements VisaTypeRepository {

    private Jdbi jdbi;

    @Autowired
    public VisaTypeRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public VisaType save(final VisaType visaType) {
        return jdbi.inTransaction(new SaveVisaTypeAction(visaType));
    }

    @Override
    public void remove(final Long id) {
        jdbi.useTransaction(new RemoveVisaTypeAction(id));
    }

    @Override
    public Optional<VisaType> findOneById(final Long id) {
        return jdbi.inTransaction(new FindVisaTypeById(id));
    }

    @Override
    public Optional<VisaType> findByName(final String name) {
        return jdbi.inTransaction(new FindVisaTypeByName(name));
    }

    @Override
    public Collection<VisaType> findAll() {
        return jdbi.withHandle(new FindAllVisaTypes());
    }

}
