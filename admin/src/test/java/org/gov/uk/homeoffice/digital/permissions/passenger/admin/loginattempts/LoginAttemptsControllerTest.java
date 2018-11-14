package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.parse;
import static java.util.Collections.emptyList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.LoginAttemptsController.DATE_FORMATTER;
import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.LoginAttemptsController.DATE_TIME_FORMATTER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptsControllerTest {

    public static final String FROM = "10/10/2017 00:00";
    public static final String TO = "11/10/2017 00:00";
    public static final String TO_DATE = "11/10/2017";


    @InjectMocks
    LoginAttemptsController testObject;

    @Mock
    LoginAttemptsService loginAttemptsService;


    @Test
    public void loginAttempts() {

        HashMap<String, Object> model = new HashMap<>();

        testObject.loginAttempts(FROM, TO, model);

        verify(loginAttemptsService, times(1)).allLoginAttemptsBetween(parse(FROM, DATE_TIME_FORMATTER), parse(TO, DATE_TIME_FORMATTER));
        assertThat(((List) model.get("attempts")).size(), is(equalTo(0)));

    }


    @Test
    public void loginAttemptsWhenUserHasMultipleLoginAttempts() {
        List<LoginAttempt> loginAttempts = Arrays.asList(new LoginAttempt("passport", "1.2.3.4", LocalDateTime.now().minusDays(2), false));

        when(loginAttemptsService.allLoginAttemptsBetween(parse(FROM, DATE_TIME_FORMATTER), parse(TO, DATE_TIME_FORMATTER))).thenReturn(loginAttempts);

        HashMap<String, Object> model = new HashMap<>();

        testObject.loginAttempts(FROM, TO, model);

        assertThat(((List) model.get("attempts")).size(), is(equalTo(1)));

    }

    @Test
    public void loginAttemptsWhenUserHasMultipleLoginAttemptsWithDefaultTime() {
        List<LoginAttempt> loginAttempts = Arrays.asList(new LoginAttempt("passport", "1.2.3.4", LocalDateTime.now().minusDays(2), false));

        when(loginAttemptsService.allLoginAttemptsBetween(parse(FROM, DATE_TIME_FORMATTER), parse(TO, DATE_TIME_FORMATTER))).thenReturn(loginAttempts);

        HashMap<String, Object> model = new HashMap<>();

        testObject.loginAttempts(FROM, TO_DATE, model);

        assertThat(((List) model.get("attempts")).size(), is(equalTo(1)));

    }


}
