package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Component
public class AuditLogoutHandler implements LogoutHandler {

    private final AuditService auditService;
    private final VisaRecordService visaRecordService;

    public AuditLogoutHandler(@Qualifier("audit.public") final AuditService auditService,
                              final VisaRecordService visaRecordService) {
        this.auditService = auditService;
        this.visaRecordService = visaRecordService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) return; // logout clicked when the session had expired
        final VisaRecord visaRecord = getVisaRecord(authentication).orElse(null);
        if (visaRecord != null) {
            String passportNumber = visaRecord.firstValueAsStringFor(VisaRuleConstants.PASSPORT_NUMBER);
            String ipAddress = RemoteIPThreadLocal.get();
            auditService.auditForPublicUser(String.format("action='logout', passportNumber='%s', IPAddress='%s'", passportNumber, ipAddress), "SUCCESS",
                    visaRecord.firstValueAsStringFor(VisaRuleConstants.FULL_NAME),
                    visaRecord.firstValueAsStringFor(VisaRuleConstants.EMAIL_ADDRESS),
                    visaRecord.firstValueAsStringFor(VisaRuleConstants.PASSPORT_NUMBER)
            );
        }
    }

    private Optional<VisaRecord> getVisaRecord(Authentication authentication) {
        return authentication != null ? ofNullable(visaRecordService.get(authentication.getName())) : empty();
    }

}
