package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository;

import lombok.NonNull;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.action.FindLoginStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.action.FindVisaCountByStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public class MiRepositoryBean implements MiRepository {

    private final Jdbi jdbi;

    @Autowired
    public MiRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Collection<Tuple<VisaStatus, Integer>> countVisaByStatus(@NonNull final LocalDateTime from,
                                                                    @NonNull final LocalDateTime to) {
        return jdbi.withHandle(new FindVisaCountByStatus(from, to));
    }

    @Override
    public Collection<Tuple<Boolean, Integer>> countSuccessfulLogin(@NonNull final LocalDateTime from,
                                                                    @NonNull final LocalDateTime to) {
        return jdbi.withHandle(new FindLoginStatus(from, to));
    }

}
