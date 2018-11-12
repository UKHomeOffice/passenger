package org.gov.uk.homeoffice.digital.permissions.passenger.issue.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.gov.uk.homeoffice.digital.permissions.passenger.issue.ui.model.TechnicalIssueForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class TechnicalIssueFormControllerTest {

    @Mock
    private NotifyService mockNotifyService;

    @InjectMocks
    private TechnicalIssueFormController underTest;

    @Test
    public void shouldGetTechnicalIssueForm() {
        ModelAndView result = underTest.GETtechnicalIssueForm();
        assertThat(result.getModel().get("issueForm"), instanceOf(TechnicalIssueForm.class));
        assertThat(result.getViewName(), is("technical-issue"));
    }

    @Test
    public void shouldPostTechnicalIssueForm() {
        final TechnicalIssueForm stubForm = new TechnicalIssueForm();
        stubForm.setEmailAddress("test@test.com");
        stubForm.setIssueDetail("This are the details");
        stubForm.setName("Error User");
        stubForm.setPassportNumber("123456789");

        final ModelAndView result = underTest.POSTtechnicalIssueForm(stubForm);

        assertThat(result.getViewName(), is("technical-issue-confirm"));

        verify(mockNotifyService).sendTechnicalIssueEmail(stubForm.getEmailAddress(),
                stubForm.getName(), stubForm.getPassportNumber(), stubForm.getIssueDetail());
        verifyNoMoreInteractions(mockNotifyService);
    }

}