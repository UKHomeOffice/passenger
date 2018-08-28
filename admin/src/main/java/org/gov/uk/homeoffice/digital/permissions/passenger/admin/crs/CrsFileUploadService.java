package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.apache.commons.io.FileUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.StorageService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleMatcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CRSVisaRecordAdapter;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.service.notify.SendEmailResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.CollectionUtils.add;

@Service
public class CrsFileUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrsFileUploadService.class);

    private final CrsFileParser crsFileParser;

    private final StorageService storageService;

    private final CrsRecordRepository crsRecordRepository;

    private final NotifyService notifyService;

    private final VisaRuleMatcher visaRuleMatcher;

    private final CRSVisaRecordAdapter crsVisaRecordAdapter;

    private final Boolean emailEnabled;

    private final String baseUrl;

    public CrsFileUploadService(CrsFileParser crsFileParser,
                                StorageService storageService,
                                CrsRecordRepository crsRecordRepository,
                                NotifyService notifyService,
                                VisaRuleMatcher visaRuleMatcher,
                                CRSVisaRecordAdapter crsVisaRecordAdapter,
                                @Value("${email.enabled}") Boolean emailEnabled,
                                @Value("${app.url}") String baseUrl) {
        this.crsFileParser = crsFileParser;
        this.storageService = storageService;
        this.crsRecordRepository = crsRecordRepository;
        this.notifyService = notifyService;
        this.visaRuleMatcher = visaRuleMatcher;
        this.crsVisaRecordAdapter = crsVisaRecordAdapter;
        this.emailEnabled = emailEnabled;
        this.baseUrl = baseUrl;
    }


    public CrsParsedResult process(File crsFile, String username) {
        CrsParsedResult parsedResult = crsFileParser.parse(crsFile);

        final List<CrsRecord> updatedRecords = parsedResult.getCrsRecords()
                                                    .stream()
                                                    .filter(crsRecord -> visaRuleMatchFound(crsRecord, parsedResult))
                                                    .peek(crsRecord -> crsRecord.setUpdatedBy(username))
                                                    .peek(crsRecord -> save(parsedResult, crsRecord))
                                                    .collect(toList());

        storeFileInS3(crsFile);

        parsedResult.setUpdatedCrsRecords(updatedRecords);

        return parsedResult;
    }

    private boolean visaRuleMatchFound(CrsRecord crsRecord, CrsParsedResult parsedResult) {
        final VisaRecord visaRecord = crsVisaRecordAdapter.getVisaRecord(crsRecord);
        return visaRuleMatcher.hasVisaRule(visaRecord, (visaRules) -> addErrors(crsRecord, parsedResult, visaRules));
    }

    private boolean addErrors(CrsRecord crsRecord, CrsParsedResult parsedResult, List<String> visaRules) {
        return visaRules != null && visaRules.stream().filter(r -> r != null).count() > 0 && parsedResult.getParseErrors().add(
                new CrsParseErrors(crsRecord.toString(), visaRules));
    }

    private void save(CrsParsedResult parsedResult, CrsRecord crsRecord) {
        try {
            crsRecordRepository.save(crsRecord);
            sendVisaEmail(crsRecord);
        } catch (Exception e) {
            addErrors(crsRecord, parsedResult, singletonList(e.getMessage()));
        }
    }

    private void storeFileInS3(File crsFile) {
        try {
            storageService.upload(crsFile.getName(), FileUtils.openInputStream(crsFile));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void sendVisaEmail(CrsRecord crsRecord) {
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
