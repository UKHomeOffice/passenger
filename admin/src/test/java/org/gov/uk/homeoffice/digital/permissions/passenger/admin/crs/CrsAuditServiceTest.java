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

import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus.REFUSED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CrsAuditServiceTest {

    public static final MockMultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(
            "crsrecords.csv",
            "originalfilename.csv",
            "text/csv",
            "\"some\", \"text\"".getBytes());

    public static final String USER = "test@test.com";

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


        testObject.audit(MOCK_MULTIPART_FILE, USER, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='0', idRange=[0-0]",
                "FAILURE",
                USER);

    }

    @Test
    public void testAuditForFileWith2Records() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(
                List.of(CrsRecord.builder().id(1L).build(),
                        CrsRecord.builder().id(2L).build()),
                Collections.emptyList());


        testObject.audit(MOCK_MULTIPART_FILE, USER, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='2', idRange=[1-2]",
                "SUCCESS",
                "test@test.com");

    }

    @Test
    public void testAuditForFileWithRevokedParticipants() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(List.of(
                CrsRecord.builder().id(1L).build(),
                CrsRecord.builder().id(2L).status(REFUSED).build(),
                CrsRecord.builder().id(3L).build(),
                CrsRecord.builder().id(4L).status(REFUSED).build()),
                List.of());


        testObject.audit(MOCK_MULTIPART_FILE, USER, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='4', idRange=[1-4]",
                "SUCCESS",
                "test@test.com");
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', revokedIds=[2,4]", "SUCCESS", USER);
    }

    @Test
    public void testAuditForFileWithErrors() {
        final CrsParsedResult crsParsedResult = new CrsParsedResult(List.of(
                CrsRecord.builder().id(1L).build(),
                CrsRecord.builder().id(2L).build()),
                List.of(
                        new CrsParseErrors("2,row,with,error", List.of("message 21", "message 22")),
                        new CrsParseErrors("4,row,with,error", List.of("message 41")))
        );


        testObject.audit(MOCK_MULTIPART_FILE, USER, crsParsedResult);

        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', numberOfRecords='2', idRange=[1-2]",
                "SUCCESS",
                "test@test.com");
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', row='2,row,with,error', error='message 21,message 22'", "FAILURE", USER);
        verify(auditService).audit("action='upload', entity='CrsRecord', fileName='originalfilename.csv', row='4,row,with,error', error='message 41'", "FAILURE", USER);

    }
}
