package org.gov.uk.homeoffice.digital.permissions.passenger.visa.ui.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class FooterController {

    @RequestMapping(value = "/footer/cookies", method = RequestMethod.GET)
    public String cookies(final Map<String, Object> model, final Authentication authentication) {
        return "footer_cookies";
    }

    @RequestMapping(value = "/footer/privacy-policy", method = RequestMethod.GET)
    public String privacyPolicy(final Map<String, Object> model, final Authentication authentication) {
        return "footer_privacy_policy";
    }

}
