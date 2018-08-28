package org.gov.uk.homeoffice.digital.permissions.passenger.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VersionController {

    private String version;

    @Autowired
    public VersionController(@Value("${app.version}") final String version) {
        this.version = version;
    }

    @GetMapping("/version")
    @ResponseBody
    public String version() {
        return version;
    }

}
