package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Controller
@RequestMapping("/loginattempts")
public class LoginAttemptsController {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private LoginAttemptsService loginAttemptsService;

    @RequestMapping(method = RequestMethod.GET)
    public String loginAttempts(@RequestParam(required = false) String from, @RequestParam(required = false) String to, Map<String, Object> model) {
        model.put("now", LocalDateTime.now());
        if(from != null && to != null){
            model.put("attempts", loginAttemptsService.allLoginAttemptsBetween(getDateTime(from), getDateTime(to)));
        }
        return "loginattempts/loginattempts";
    }

    private LocalDateTime getDateTime(@RequestParam(required = false) String from) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(from, DATE_TIME_FORMATTER);
        } catch(DateTimeParseException e){
            localDateTime = LocalDate.parse(from, DATE_FORMATTER).atTime(0,0);
        }
        return localDateTime;
    }
}
