package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.apache.commons.lang3.StringUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrsAuditService {

    @Autowired
    @Qualifier("audit.admin")
    private AuditService auditService;

    public void audit(String username, String result, String message) {
        auditService.audit(new Audit(null, username, LocalDateTime.now(), result, message));
    }

    public void audit(MultipartFile file, String username, CrsParsedResult result) {
        final List<CrsRecord> crsRecords = result.getCrsRecords()
                .stream()
                .filter(crsRecord -> crsRecord.getId() != null && crsRecord.getId() > 0)
                .collect(Collectors.toList());

        int size = crsRecords.size();
        long idFrom = 0;
        long idTo = 0;
        if (size > 0) {
            idFrom = crsRecords.get(0).getId();
            idTo = crsRecords.get(size - 1).getId();
        }

        auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', numberOfRecords='%s', idRange=[%s-%s]",
                file.getOriginalFilename(), size, idFrom, idTo),
                size > 0 ? "SUCCESS" : "FAILURE",
                username);

        result.getParseErrors()
                .stream()
                .forEach(parseError -> auditService.audit(
                        String.format("action='upload', entity='CrsRecord', fileName='%s', row='%s', error='%s'",
                                file.getOriginalFilename(),
                                escape(parseError.crsRow),
                                escape(parseError.message.stream().collect(Collectors.joining(",")))),
                        "FAILURE", username));

        String revokedIds = result.getCrsRecords()
                .stream()
                .filter(crsRecord -> crsRecord.getStatus() == VisaStatus.REVOKED)
                .map(crsRecord -> String.valueOf(crsRecord.getId()))
                .collect(Collectors.joining(","));

        if (!StringUtils.isEmpty(revokedIds)) {
            auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', revokedIds=[%s]",
                    file.getOriginalFilename(), revokedIds), "SUCCESS", username);
        }
    }

    private String escape(String s) {
        return s.replaceAll("'", "\\\\'");
    }


}
