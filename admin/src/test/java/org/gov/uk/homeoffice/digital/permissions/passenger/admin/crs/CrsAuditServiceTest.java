package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.List;

import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus.ISSUED;
import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus.REFUSED;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CrsAuditServiceTest {

    public static final MockMultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(
            "crsrecords.csv",
            "originalfilename.csv",
            "text/csv",
            "\"some\", \"text\"".getBytes());

    @Mock
    AuditService auditService;

    @InjectMocks
    CrsAuditService testObject;


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAuditForFileWithNoRecords() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(Collections.emptyList(), Collections.emptyList());

        testObject.audit(MOCK_MULTIPART_FILE, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='0', idRange=[0-0]",
                "FAILURE",
                null, null, null);

    }

    @Test
    public void testAuditForFileWith2Records() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(
                List.of(CrsRecord.builder().id(1L).postName("James").familyName("SMITH").emailAddress("jamessmith@test.com").passportNumber("101010101").status(ISSUED).build(),
                        CrsRecord.builder().id(2L).postName("Steve").familyName("SMITH").emailAddress("stevesmith@test.com").passportNumber("101010102").status(ISSUED).build()),
                Collections.emptyList());

        testObject.audit(MOCK_MULTIPART_FILE, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='2', idRange=[1-2]",
                "SUCCESS",
                "James SMITH", "jamessmith@test.com", "101010101");

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='2', idRange=[1-2]",
                "SUCCESS",
                "Steve SMITH", "stevesmith@test.com", "101010102");
    }

    @Test
    public void testAuditForFileWithRevokedParticipants() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(List.of(
                CrsRecord.builder().id(1L).postName("James").familyName("SMITH").emailAddress("jamessmith@test.com").passportNumber("101010101").status(ISSUED).build(),
                CrsRecord.builder().id(2L).postName("Steve").familyName("SMITH").emailAddress("stevesmith@test.com").passportNumber("101010102").status(REFUSED).build(),
                CrsRecord.builder().id(3L).postName("Jane").familyName("SMITH").emailAddress("janesmith@test.com").passportNumber("101010103").status(ISSUED).build(),
                CrsRecord.builder().id(4L).postName("Stephanie").familyName("SMITH").emailAddress("stephaniesmith@test.com").passportNumber("101010104").status(REFUSED).build()),
                List.of());

        testObject.audit(MOCK_MULTIPART_FILE, crsParsedResult);

        // All uploads
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='4', idRange=[1-4]",
                "SUCCESS",
                "James SMITH", "jamessmith@test.com", "101010101");

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='4', idRange=[1-4]",
                "SUCCESS",
                "Steve SMITH", "stevesmith@test.com", "101010102");

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='4', idRange=[1-4]",
                "SUCCESS",
                "Jane SMITH", "janesmith@test.com", "101010103");

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='4', idRange=[1-4]",
                "SUCCESS",
                "Stephanie SMITH", "stephaniesmith@test.com", "101010104");

        // Revoked
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', revokedId=2", "SUCCESS",
                "Steve SMITH", "stevesmith@test.com", "101010102");

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', revokedId=4", "SUCCESS",
                "Stephanie SMITH", "stephaniesmith@test.com", "101010104");
    }

    @Test
    public void testAuditForFileWithErrors() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(List.of(
                CrsRecord.builder().id(1L).postName("James").familyName("SMITH").emailAddress("jamessmith@test.com").passportNumber("101010101").status(ISSUED).build(),
                CrsRecord.builder().id(2L).postName("Steve").familyName("SMITH").emailAddress("stevesmith@test.com").passportNumber("101010102").status(REFUSED).build()),
                List.of(
                        new CrsParseErrors("2,row,with,error", List.of("message 21", "message 22")),
                        new CrsParseErrors("4,row,with,error", List.of("message 41")))
        );

        testObject.audit(MOCK_MULTIPART_FILE, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='2', idRange=[1-2]",
                "SUCCESS",
                "James SMITH", "jamessmith@test.com", "101010101");
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', row='2,row,with,error', error='message 21,message 22'", "FAILURE",
                null, null, null);
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', row='4,row,with,error', error='message 41'", "FAILURE",
                null, null, null);
    }
}
