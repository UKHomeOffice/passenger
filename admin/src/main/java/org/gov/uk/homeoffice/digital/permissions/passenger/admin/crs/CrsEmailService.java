package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.service.notify.SendEmailResponse;

import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.CollectionUtils.add;

@Service
public class CrsEmailService {

    private final NotifyService notifyService;
    private final Boolean emailEnabled;
    private final String baseUrl;
    private final CrsRecordRepository crsRecordRepository;


    public CrsEmailService(NotifyService notifyService,
                           @Value("${emai" +
                                   "l.enabled}") Boolean emailEnabled,
                           @Value("${app.url}") String baseUrl,
                           CrsRecordRepository crsRecordRepository) {
        this.notifyService = notifyService;
        this.emailEnabled = emailEnabled;
        this.baseUrl = baseUrl;
        this.crsRecordRepository = crsRecordRepository;
    }

    public void sendVisaEmail(CrsRecord crsRecord) {
        if (emailEnabled) {
            final Set<String> emailsSent = crsRecordRepository.getByPassportNumber(crsRecord.getPassportNumber())
                    .map(part -> part.getEmailsSent())
                    .orElse(emptySet());
            if (visaGrantedToBeSend(crsRecord, emailsSent)) {
                final Optional<SendEmailResponse> response = notifyService.sendVisaGrantedEmail(crsRecord.getEmailAddress(), crsRecord.getOtherName(), crsRecord.getFamilyName(), baseUrl);
                if (response.isPresent()) {
                    crsRecord.setEmailsSent(add(emailsSent, "GRANTED"));
                    crsRecordRepository.save(crsRecord);
                }
            }
            if (visaRevokedToBeSend(crsRecord, emailsSent)) {
                final Optional<SendEmailResponse> response = notifyService.sendVisaRevokedEmail(crsRecord.getEmailAddress(), crsRecord.getOtherName(), crsRecord.getFamilyName(), baseUrl);
                if (response.isPresent()) {
                    crsRecord.setEmailsSent(add(emailsSent, "REVOKED"));
                    crsRecordRepository.save(crsRecord);
                }
            }
        }

    }

    private boolean visaGrantedToBeSend(CrsRecord crsRecord, Set<String> emailsSent) {
        return crsRecord.getStatus() == VisaStatus.VALID && !emailsSent.contains("GRANTED");
    }

    private boolean visaRevokedToBeSend(CrsRecord crsRecord, Set<String> emailsSent) {
        return crsRecord.getStatus() == VisaStatus.REVOKED && !emailsSent.contains("REVOKED");
    }
}
