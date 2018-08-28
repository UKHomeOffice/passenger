package org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.model.RuleContentForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RuleContentController {

    private VisaRuleLookupRepository visaRuleLookupRepository;

    @Autowired
    public RuleContentController(final VisaRuleLookupRepository visaRuleLookupRepository) {
        this.visaRuleLookupRepository = visaRuleLookupRepository;
    }

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public ModelAndView displayRuleContentEditor() {
//        final List<VisaRule> visaRuleCollection = new ArrayList<>(visaRuleLookupRepository.findAllEditable());
//
//        final RuleContentForm ruleContentForm = new RuleContentForm();
//        ruleContentForm.setVisaRules(visaRuleCollection);

//        return new ModelAndView("rules/rulesContent" , "ruleContentForm", ruleContentForm);
        return null;
    }

    @RequestMapping(value = "/updateRuleContent", method = RequestMethod.POST)
    public ModelAndView updateRuleContent(@ModelAttribute(value="ruleContentForm") final RuleContentForm ruleContentForm) {
        ruleContentForm.getVisaRules().forEach(visaRule -> visaRuleLookupRepository.save(visaRule));
        return new ModelAndView("rules/rulesContent", "ruleContentForm", ruleContentForm);
    }

}
