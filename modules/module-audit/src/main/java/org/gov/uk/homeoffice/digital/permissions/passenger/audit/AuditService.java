package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Collection;

import static java.time.LocalDateTime.now;

@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);
    public static final String PASSENGER = "PASSENGER";

    private final Jdbi dbi;
    private final Counter counter;

    @Autowired
    public AuditService(final Jdbi dbi, final MeterRegistry meterRegistry) {
        this.dbi = dbi;
        this.counter = meterRegistry.counter("audit", "error", "count");
    }

    public void audit(final String action,
                      final String result) {
        audit(action, result, null, null, null);
    }

    public void audit(final String action,
                      final String result,
                      final String passengerName,
                      final String passengerEmail,
                      final String passengerPassportNumber) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        audit(action, result, user,
                passengerName, passengerEmail, passengerPassportNumber);
    }

    public void auditForPublicUser(final String action,
                      final String result,
                      final String passengerName,
                      final String passengerEmail,
                      final String passengerPassportNumber) {
        audit(action, result, PASSENGER,
                passengerName, passengerEmail, passengerPassportNumber);
    }

    public void audit(final String action, final String result, final String user,
                      final String passengerName, final String passengerEmail, final String passengerPassportNumber) {
        audit(new Audit(null, user, now(), result, action, passengerName, passengerEmail, passengerPassportNumber));
    }

    public void audit(final Audit audit) {
        try {
            final AuditDAO dao = dbi.onDemand(AuditDAO.class);
            dao.insert(audit);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            counter.count();
        }
    }

    public Collection<Audit> findByPassportNumber(final String passportNumber) {
        return dbi.withHandle(new FindByPassportNumber(passportNumber));
    }

    public Collection<Audit> findByPassengerEmail(final String emailAddress) {
        return dbi.withHandle(new FindByPassengerEmail(emailAddress));
    }

    public Collection<Audit> findByPassengerName(final String name) {
        return dbi.withHandle(new FindByPassengerName(name));
    }

    public Collection<Audit> findByAdminEmail(final String emailAddress) {
        return dbi.withHandle(new FindByAdminEmail(emailAddress));
    }

    public Collection<Audit> findByQuery(final String adminEmailAddress,
                                         final String passengerEmailAddress,
                                         final String passengerPassportNumber,
                                         final String passengerName,
                                         final LocalDate from,
                                         final LocalDate to) {
        return dbi.withHandle(new FindByQuery(adminEmailAddress, passengerEmailAddress,
                passengerPassportNumber, passengerName, from, to));
    }

    public Collection<Audit> findByDateRange(final LocalDate from, final LocalDate to) {
        return dbi.withHandle(new FindByDateRange(from, to));
    }

}
