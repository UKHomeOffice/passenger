package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.apache.commons.io.FileUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.StorageService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleMatcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CRSVisaRecordAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
public class CrsFileUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrsFileUploadService.class);

    private final CrsFileParser crsFileParser;

    private final StorageService storageService;

    private final CrsRecordRepository crsRecordRepository;

    private final VisaRuleMatcher visaRuleMatcher;

    private final CRSVisaRecordAdapter crsVisaRecordAdapter;

    private final CrsEmailService crsEmailService;

    private final VisaTypeRepository visaTypeRepository;

    public CrsFileUploadService(CrsFileParser crsFileParser,
                                StorageService storageService,
                                CrsRecordRepository crsRecordRepository,
                                VisaRuleMatcher visaRuleMatcher,
                                CRSVisaRecordAdapter crsVisaRecordAdapter,
                                CrsEmailService crsEmailService,
                                VisaTypeRepository visaTypeRepository) {
        this.crsFileParser = crsFileParser;
        this.storageService = storageService;
        this.crsRecordRepository = crsRecordRepository;
        this.visaRuleMatcher = visaRuleMatcher;
        this.crsVisaRecordAdapter = crsVisaRecordAdapter;
        this.crsEmailService = crsEmailService;
        this.visaTypeRepository = visaTypeRepository;
    }


    public CrsParsedResult process(File crsFile, String username) {
        final CrsParsedResult parsedResult = crsFileParser.parse(crsFile);

        final List<CrsRecord> updatedRecords = parsedResult.getCrsRecords()
                                                    .stream()
                                                    .filter(crsRecord -> visaRuleMatchFound(crsRecord, parsedResult))
                                                    .peek(crsRecord -> crsRecord.setVisaEndorsementDescription(visaTypeRepository.findByName(crsRecord.getVisaEndorsement())))
                                                    .peek(crsRecord -> crsRecord.setUpdatedBy(username))
                                                    .peek(crsRecord -> save(parsedResult, crsRecord))
                                                    .collect(toList());

        storeFileInS3(crsFile);

        return parsedResult.withUpdatedCrsRecords(updatedRecords);
    }

    private boolean visaRuleMatchFound(CrsRecord crsRecord, CrsParsedResult parsedResult) {
        if (crsRecord.isInvalid()) {
            parsedResult.getParseErrors().add(new CrsParseErrors("Empty row", List.of("CRS record is invalid.")));
            return false;
        } else {
            final VisaRecord visaRecord = crsVisaRecordAdapter.getVisaRecord(crsRecord);
            return visaRuleMatcher.hasVisaRule(visaRecord, (visaRules) -> addErrors(crsRecord, parsedResult, visaRules));
        }
    }

    private boolean addErrors(CrsRecord crsRecord, CrsParsedResult parsedResult, List<String> visaRules) {
        return visaRules != null && visaRules.stream().anyMatch(Objects::nonNull) && parsedResult.getParseErrors().add(
                new CrsParseErrors(crsRecord.toString(), visaRules));
    }

    private void save(CrsParsedResult parsedResult, CrsRecord crsRecord) {
        try {
            crsRecordRepository.save(crsRecord);
            crsEmailService.sendVisaEmail(crsRecord);
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

}
