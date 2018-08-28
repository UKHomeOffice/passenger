package org.gov.uk.homeoffice.digital.permissions.passenger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String redirectToVisaPage() {
        return "redirect:/visa/2fa";
    }

    @RequestMapping(value = "/help")
    public String help() {
        return "help/help";
    }

}
