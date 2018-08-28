package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CsvRowCreator {

    public String nameCheckRow(VisaRecord visaRecord) {
        return String.format("%sA01,P,%s,98765431,%s,%s,%s,%s",
                visaRecord.firstValueAsStringFor(VisaRuleConstants.VAF_NUMBER),
                visaRecord.firstValueAsStringFor(VisaRuleConstants.NATIONALITY),
                visaRecord.firstValueAsStringFor(VisaRuleConstants.SURNAME),
                filter(visaRecord.firstValueAsStringFor(VisaRuleConstants.FULL_NAME),
                       visaRecord.firstValueAsStringFor(VisaRuleConstants.SURNAME)),
                date(visaRecord.firstValueAsStringFor(VisaRuleConstants.DATE_OF_BIRTH)),
                visaRecord.firstValueAsStringFor(VisaRuleConstants.NATIONALITY));
    }

    public String docCheckRow(VisaRecord visaRecord) {
        return String.format("%sA01,P,%s,%s,DOC CHECK,,%s,%s",
                visaRecord.firstValueAsStringFor(VisaRuleConstants.VAF_NUMBER),
                visaRecord.firstValueAsStringFor(VisaRuleConstants.NATIONALITY),
                visaRecord.firstValueAsStringFor(VisaRuleConstants.PASSPORT_NUMBER),
                date(visaRecord.firstValueAsStringFor(VisaRuleConstants.DATE_OF_BIRTH)),
                visaRecord.firstValueAsStringFor(VisaRuleConstants.NATIONALITY));
    }

    private String filter(String s1, String s2) {
        return s1.replace(s2, "").trim();
    }

    private String date(String isoDate) {
        return LocalDate.parse(isoDate, DateTimeFormatter.BASIC_ISO_DATE).format(DateTimeFormatter.ofPattern("dd,MM,yyyy"));
    }


}
