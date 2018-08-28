package org.gov.uk.homeoffice.digital.permissions.passenger.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginFail(RedirectAttributes redirectAttributes,
                            @SessionAttribute(name = "SPRING_SECURITY_LAST_EXCEPTION") AuthenticationException authenticationException) {
        if (authenticationException instanceof BadCredentialsException) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        if (authenticationException instanceof LockedException) {
            redirectAttributes.addFlashAttribute("locked", true);
        }

        return "redirect:/login";
    }
}
