package org.gov.uk.homeoffice.digital.permissions.passenger.admin;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void globalAttributes(Map<String, Object> model, Authentication authentication, HttpServletResponse response) {

        model.put("authenticated", authentication != null && authentication.isAuthenticated());
        model.put("status", response.getStatus());
    }
}
