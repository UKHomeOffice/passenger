package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

@Service
public class AuditService {

    public static final Authentication AUTHENTICATION = SecurityContextHolder.getContext().getAuthentication();

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private final Jdbi dbi;
    private final Counter counter;

    @Autowired
    public AuditService(final Jdbi dbi, final MeterRegistry meterRegistry) {
        this.dbi = dbi;
        this.counter = meterRegistry.counter("audit", "error", "count");
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
        audit(action, result, passengerEmail,
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

}
