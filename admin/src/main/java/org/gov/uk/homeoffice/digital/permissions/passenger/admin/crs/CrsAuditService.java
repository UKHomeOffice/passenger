package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

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
        auditService.audit(new Audit(null, username, LocalDateTime.now(), result, message, null, null, null));
    }

    public void audit(MultipartFile file, CrsParsedResult result) {
        final List<CrsRecord> crsRecords = result.getCrsRecords()
                .stream()
                .filter(crsRecord -> crsRecord.getId() != null && crsRecord.getId() > 0)
                .collect(Collectors.toList());

        int size = crsRecords.size();

        final long idFrom = (size > 0) ? crsRecords.get(0).getId() : 0;
        final long idTo = (size > 0) ? crsRecords.get(size - 1).getId() : 0;

        if (size == 0) {
            auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', numberOfRecords='%s', idRange=[%s-%s]",
                    file.getOriginalFilename(), size, idFrom, idTo),
                    "FAILURE", null, null, null);
        }

        crsRecords.forEach(record ->
            auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', numberOfRecords='%s', idRange=[%s-%s]",
                file.getOriginalFilename(), size, idFrom, idTo),
                size > 0 ? "SUCCESS" : "FAILURE",
                record.getFullName(), record.getEmailAddress(), record.getPassportNumber()));

        result.getParseErrors()
                .forEach(parseError -> auditService.audit(
                        String.format("action='upload', entity='CrsRecord', fileName='%s', row='%s', error='%s'",
                                file.getOriginalFilename(),
                                escape(parseError.crsRow),
                                escape(String.join(",", parseError.message))),
                        "FAILURE", null, null, null));

        result.getCrsRecords().stream()
                .filter(crsRecord -> crsRecord.getStatus().equals(VisaStatus.REFUSED))
                .forEach(record -> {
                    auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', revokedId=%s",
                            file.getOriginalFilename(), record.getId()), "SUCCESS",
                            record.getFullName(), record.getEmailAddress(), record.getPassportNumber());
                });
    }

    private String escape(String s) {
        return s.replaceAll("'", "\\\\'");
    }


}
