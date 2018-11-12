package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model.AuditDateRangeForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuditExportControllerTest {

    @InjectMocks
    private AuditExportController underTest;

    private AuditService mockAdminAuditService;
    private AuditService mockPublicAuditService;
    private BindingResult mockBindingResult;

    @Before
    public void before() {
        mockAdminAuditService = mock(AuditService.class);
        mockPublicAuditService = mock(AuditService.class);
        mockBindingResult = mock(BindingResult.class);

        ReflectionTestUtils.setField(underTest, "adminAuditSearch", mockAdminAuditService);
        ReflectionTestUtils.setField(underTest, "publicAuditSearch", mockPublicAuditService);
    }

    @Test
    public void shouldGetAuditPage() {
        ModelAndView result = underTest.GETauditExport();
        assertThat(result.getViewName(), is("audit/audit-export"));
    }

    @Test
    public void shouldExportAudit() throws IOException {
        final LocalDate from = LocalDate.of(2018, 10, 1);
        final LocalDate to = LocalDate.of(2018, 10, 31);

        final AuditDateRangeForm form = new AuditDateRangeForm();
        form.setFrom(from);
        form.setTo(to);

        final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        final PrintWriter mockWriter = mock(PrintWriter.class);

        final Audit audit1 = new Audit(1L, "audit1", LocalDateTime.of(2018, 10, 5, 10, 30),
                "result", "ccntent", "passenger name", "passenger email", "passport number");
        final Audit audit2 = new Audit(2L, "audit2", LocalDateTime.of(2018, 10, 7, 8, 15),
                "result", "ccntent", "passenger name", "passenger email", "passport number");

        when(mockAdminAuditService.findByDateRange(from, to)).thenReturn(List.of(audit1));
        when(mockPublicAuditService.findByDateRange(from, to)).thenReturn(List.of(audit2));
        when(mockResponse.getWriter()).thenReturn(mockWriter);
        when(mockBindingResult.hasErrors()).thenReturn(false);

        underTest.POSTauditExport(form,  mockBindingResult, null, mockResponse);

        verify(mockResponse).setContentType("text/csv");
        verify(mockResponse).setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=\"%s\"", "audit-export.csv"));
    }

}