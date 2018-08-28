package org.gov.uk.homeoffice.digital.permissions.passenger.visa.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.gov.uk.homeoffice.digital.permissions.passenger.security.TOTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Strings.repeat;
import static java.util.Optional.ofNullable;
import static org.assertj.core.util.Strings.concat;

@Controller
public class TwoFactorAuthController {

    private NotifyService notifyService;
    private VisaRecordService visaRecordService;
    private TOTP totp;

    @Autowired
    public TwoFactorAuthController(final NotifyService notifyService,
                                   final VisaRecordService visaRecordService,
                                   final TOTP totp) {
        this.notifyService = notifyService;
        this.visaRecordService = visaRecordService;
        this.totp = totp;
    }

    @RequestMapping(value = "/visa/2fa")
    public ModelAndView display(final Authentication authentication) {
        if (isGrantedAuthority(authentication))
                return new ModelAndView("redirect:/visa/details");

        final VisaRecord record = ofNullable(visaRecordService.get(authentication.getPrincipal().toString()))
                .orElseThrow(RuntimeException::new);

        final String code = totp.generateCode();
        final String mobileNumber = record.firstValueAsStringFor(VisaRuleConstants.MOBILE_NUMBER);

        notifyService.sendTwoFactorSMS(mobileNumber, code);

        final ModelAndView modelAndView = new ModelAndView("check_your_phone");
        modelAndView.getModel().put("mobileNumber", obfuscateMobileNumber(mobileNumber));

        return modelAndView;
    }

    @RequestMapping(value = "/visa/2fa", method = RequestMethod.POST)
    public String next() {
        grantAuthority();
        return "redirect:/visa/details";
    }

    private String obfuscateMobileNumber(final String mobileNumber) {
        return concat(repeat("X", mobileNumber.length() - 4),
                mobileNumber.substring(mobileNumber.length() - 4));
    }

    private boolean isGrantedAuthority(final Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    }

    private void grantAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = new ArrayList<>(auth.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
