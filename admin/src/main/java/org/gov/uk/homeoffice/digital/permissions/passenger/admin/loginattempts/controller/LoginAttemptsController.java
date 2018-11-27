package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.model.LoginAttemptsDateRangeForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.service.LoginAttemptsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/loginattempts")
public class LoginAttemptsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptsController.class);

//    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private LoginAttemptsService loginAttemptsService;

    @GetMapping
    public ModelAndView GETshowLoginAttempts() {
        LOGGER.debug("Login attempts");
        return new ModelAndView("loginattempts/loginattempts", "loginAttemptsDateRangeForm",
                new LoginAttemptsDateRangeForm());
    }

    @PostMapping
    public String POSTshowLoginAttempts(@ModelAttribute(value="loginAttemptsDateRangeForm") final LoginAttemptsDateRangeForm dateRangeForm,
                                        final Model model) {
        final LocalDate fromDate = getFromDate(dateRangeForm.getFrom());
        final LocalDate toDate = getToDate(dateRangeForm.getTo());

        LOGGER.debug("Showing attempts between {} and {}", fromDate, toDate);

        if(fromDate != null && toDate != null){
            model.addAttribute("attempts", loginAttemptsService.allLoginAttemptsBetween(
                    fromDate.atTime(LocalTime.MIN), toDate.atTime(LocalTime.MAX)));
        }

        return "loginattempts/loginattempts";
    }

    private LocalDate getToDate(LocalDate to) {
        return to != null ? to : LocalDate.MAX;
    }

    private LocalDate getFromDate(LocalDate from) {
        return from != null ? from : LocalDate.MIN;
    }

}
