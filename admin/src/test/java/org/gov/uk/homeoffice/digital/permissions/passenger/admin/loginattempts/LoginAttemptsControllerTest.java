package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.controller.LoginAttemptsController;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.model.LoginAttemptsDateRangeForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.service.LoginAttemptsService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptsControllerTest {

    @InjectMocks
    private LoginAttemptsController testObject;

    @Mock
    private LoginAttemptsService loginAttemptsService;

    @Test
    public void loginAttempts() {
        Model mockModel = mock(Model.class);

        LocalDate from = LocalDate.now().minus(10, ChronoUnit.DAYS);
        LocalDate to = LocalDate.now();

        LoginAttemptsDateRangeForm form = new LoginAttemptsDateRangeForm();
        form.setFrom(from);
        form.setTo(to);

        testObject.POSTshowLoginAttempts(form, mockModel);

        verify(loginAttemptsService, times(1)).allLoginAttemptsBetween(from.atTime(LocalTime.MIN), to.atTime(LocalTime.MAX));
    }

    @Test
    public void loginAttemptsWhenUserHasMultipleLoginAttempts() {

        Model mockModel = mock(Model.class);

        LocalDate from = LocalDate.now().minus(10, ChronoUnit.DAYS);
        LocalDate to = LocalDate.now();

        LoginAttemptsDateRangeForm form = new LoginAttemptsDateRangeForm();
        form.setFrom(from);
        form.setTo(to);

        List<LoginAttempt> loginAttempts = Arrays.asList(new LoginAttempt("passport", "1.2.3.4", LocalDateTime.now().minusDays(2), false));

        when(loginAttemptsService.allLoginAttemptsBetween(from.atTime(LocalTime.MIN), to.atTime(LocalTime.MAX))).thenReturn(loginAttempts);

        testObject.POSTshowLoginAttempts(form, mockModel);

        verify(mockModel).addAttribute("attempts", loginAttempts);
    }

}
