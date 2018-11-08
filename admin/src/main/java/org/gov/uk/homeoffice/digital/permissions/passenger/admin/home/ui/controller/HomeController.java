package org.gov.uk.homeoffice.digital.permissions.passenger.admin.home.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(path = "/")
    public String GEThome() {
        return "home";
    }

}
