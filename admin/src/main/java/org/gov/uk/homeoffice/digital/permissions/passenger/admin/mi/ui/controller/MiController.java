package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.service.MiService;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.ui.model.MiModel;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Controller
@RequestMapping("/mi")
public class MiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiController.class);

    private final MiService miService;

    @Autowired
    public MiController(final MiService miService) {
        this.miService = miService;
    }

    @GetMapping
    public ModelAndView GETmi() {
        final LocalDate now = LocalDate.now();

        final MiModel model = new MiModel();
        model.setMonth(now.getMonth().getValue());
        model.setYear(now.getYear());

        LOGGER.debug("Setting MI date range to today");

        return POSTmi(model);
    }

    @PostMapping
    public ModelAndView POSTmi(@ModelAttribute(value="miModel") final MiModel model) {
        model.setVisaStatusData(generateVisaStatusData(miService.visaCountByStatusForMonth(Month.of(model.getMonth()),
                model.getYear())));
        model.setLoginData(generateLoginData(miService.loginCountForMonth(Month.of(model.getMonth()),
                model.getYear())));

        return new ModelAndView("mi/mi", "miModel", model);
    }

    private int[] generateVisaStatusData(final Collection<Tuple<VisaStatus, Integer>> data) {
        final int granted = data.stream()
                .filter(tpl -> tpl.get_1().equals(VisaStatus.ISSUED))
                .mapToInt(Tuple::get_2).sum();
        final int refused = data.stream()
                .filter(tpl -> tpl.get_1().equals(VisaStatus.REFUSED))
                .mapToInt(Tuple::get_2).sum();
        return new int[] { granted, refused };
    }

    private int[] generateLoginData(final Collection<Tuple<Boolean, Integer>> data) {
        final int success = data.stream()
                .filter(tpl -> tpl.get_1().equals(Boolean.TRUE))
                .mapToInt(Tuple::get_2).sum();
        final int failure = data.stream()
                .filter(tpl -> tpl.get_1().equals(Boolean.FALSE))
                .mapToInt(Tuple::get_2).sum();
        return new int[] { success, failure };
    }

}
