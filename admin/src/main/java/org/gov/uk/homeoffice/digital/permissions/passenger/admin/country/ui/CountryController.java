package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.ui;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service.CountryService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CountryController implements Serializable {

    private final CountryService countryService;

    @Autowired
    public CountryController(final CountryService countryService) {
        this.countryService = countryService;
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public String show(@RequestParam(name = "filter", defaultValue = "all") final String filterString,
                       final Model model) {
        final String filter = filterString.equalsIgnoreCase("selected") ? "selected" : "all";
        final CountryListForm form = new CountryListForm();
        final List<Country> countryList = new ArrayList<>(countryService.getCountries()).stream()
                .filter(c -> (filter.equals("all")) ? true : c.getEnabled())
                .sorted(Comparator.comparing(Country::getDisplay))
                .collect(Collectors.toList());
        form.setCountries(countryList);
        model.addAttribute("form", form);
        return "country/country";
    }

    @RequestMapping(value = "/countries", method = RequestMethod.POST)
    public String update(@ModelAttribute(value = "form") final CountryListForm form,
                                    final Model model) {
        form.getCountries().forEach(country -> {
            countryService.getCountryByCountryCode(country.getId()).ifPresent(updateCountry -> {
                updateCountry.setExportCountry(country.getExportCountry());
                updateCountry.setEnabled(country.getEnabled());
                countryService.saveCountry(updateCountry);
            });
        });
        return "redirect:/countries";
    }

}
