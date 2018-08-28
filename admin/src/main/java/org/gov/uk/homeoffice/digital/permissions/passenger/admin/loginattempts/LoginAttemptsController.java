package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/loginattempts")
public class LoginAttemptsController {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private LoginAttemptsService loginAttemptsService;

    @RequestMapping(method = RequestMethod.GET)
    public String loginAttempts(@RequestParam(required = false) String from, @RequestParam(required = false) String to, Map<String, Object> model) {
        model.put("now", LocalDateTime.now());
        if(from != null && to != null){
            model.put("attempts", loginAttemptsService.allLoginAttemptsBetween(LocalDateTime.parse(from, FORMATTER), LocalDateTime.parse(to, FORMATTER)));
        }
        return "loginattempts/loginattempts";
    }
}
