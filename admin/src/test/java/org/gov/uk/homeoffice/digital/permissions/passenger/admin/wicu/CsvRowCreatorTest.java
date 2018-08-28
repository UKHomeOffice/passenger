package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvRowCreatorTest {

    private CsvRowCreator csvRowCreator = new CsvRowCreator();

    @Test
    public void createDocCheckCsvRow() {
        VisaRecord mockVisaRecord = mock(VisaRecord.class);

        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.VAF_NUMBER)).thenReturn("1234567");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.NATIONALITY)).thenReturn("USA");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.PASSPORT_NUMBER)).thenReturn("534931234");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.SURNAME)).thenReturn("Dough");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.FULL_NAME)).thenReturn("Jane Dough");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.DATE_OF_BIRTH)).thenReturn("19990818");

        assertThat(csvRowCreator.docCheckRow(mockVisaRecord),
                is("1234567A01,P,USA,534931234,DOC CHECK,,18,08,1999,USA"));
    }

    @Test
    public void createNameCheckCsvRow() {
        VisaRecord mockVisaRecord = mock(VisaRecord.class);

        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.VAF_NUMBER)).thenReturn("1234567");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.NATIONALITY)).thenReturn("USA");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.SURNAME)).thenReturn("Dough");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.FULL_NAME)).thenReturn("Jane Dough");
        when(mockVisaRecord.firstValueAsStringFor(VisaRuleConstants.DATE_OF_BIRTH)).thenReturn("19990818");

        assertThat(csvRowCreator.nameCheckRow(mockVisaRecord),
                is("1234567A01,P,USA,98765431,Dough,Jane,18,08,1999,USA"));
    }
}
