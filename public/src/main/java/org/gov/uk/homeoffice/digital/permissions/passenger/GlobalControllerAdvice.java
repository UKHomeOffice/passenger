package org.gov.uk.homeoffice.digital.permissions.passenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${passenger.tracking.google.token}")
    private String googleToken;

    @ModelAttribute
    public void globalAttributes(Model model, Authentication authentication) {

        model.addAttribute("authenticated", authentication != null && authentication.isAuthenticated());
        model.addAttribute("googleToken", googleToken);
    }
}
