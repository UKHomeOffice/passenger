package org.gov.uk.homeoffice.digital.permissions.passenger.authentication;

import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoginControllerTest {

    @Test
    public void shouldReturnLoginForm() {
        LoginController controller = new LoginController();
        assertThat(controller.loginForm(), is("login"));
    }

    @Test
    public void shouldReturnLoginFailedForIncorrectCredentials() {
        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);
        LoginController controller = new LoginController();
        assertThat(controller.loginFail(mockRedirectAttributes, new BadCredentialsException("bad creds")), is("redirect:/login"));
        verify(mockRedirectAttributes).addFlashAttribute("error", true);
    }

    @Test
    public void shouldReturnLoginFailedForLockedAccount() {
        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);
        LoginController controller = new LoginController();
        assertThat(controller.loginFail(mockRedirectAttributes, new LockedException("locked")), is("redirect:/login"));
        verify(mockRedirectAttributes).addFlashAttribute("locked", true);
    }

}