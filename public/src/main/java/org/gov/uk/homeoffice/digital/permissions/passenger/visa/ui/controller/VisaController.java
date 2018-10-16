package org.gov.uk.homeoffice.digital.permissions.passenger.visa.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.visa.rule.DynamicContentProcessor;
import org.gov.uk.homeoffice.digital.permissions.passenger.visa.ui.model.VisaStatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.fromDisplayDate;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.parse;

@Controller
public class VisaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisaController.class);

    public static final int MAX_TRAVEL_DAYS = 30;

    public static final String POLICE_REGISTERATION_REQUIRED = "Based on the details you provided in your visa application, you need to register with the police.";
    public static final String POLICE_REGISTERATION_NOT_REQUIRED = "Based on the details you provided in your visa application, you do not need to register with the police.";

    private final VisaTypeService visaTypeService;
    private final VisaRecordService visaRecordService;
    private final DynamicContentProcessor dynamicContentProcessor;
    private final AuditService auditService;

    @Autowired
    public VisaController(final VisaTypeService visaTypeService,
                          final VisaRecordService visaRecordService,
                          final DynamicContentProcessor dynamicContentProcessor,
                          final @Qualifier("audit.public") AuditService auditService) {
        this.visaTypeService = visaTypeService;
        this.visaRecordService = visaRecordService;
        this.dynamicContentProcessor = dynamicContentProcessor;
        this.auditService = auditService;
    }

    @RequestMapping(value = "/visa/details", method =  RequestMethod.GET)
    public String visaDetails(final Map<String, Object> model, final Authentication authentication) {
        final Optional<VisaRecord> visaRecord = getVisaRecord(authentication);
        if(visaRecord.isPresent() && visaRecord.get().getVisaStatus().equals(VisaStatus.ISSUED)){
            final VisaRecord record = visaRecord.get();
            visaTypeService.findVisaTypeRule(record).get_1().
                    ifPresentOrElse(visaTypeRule -> model.put("visa", new VisaStatusModel(dynamicContentProcessor, record, visaTypeRule))
                            , () -> LOGGER.error("Unable to find a valid status."));
            audit("action='Check your visa'", "SUCCESS", record);
            return "check_your_visa_details";
        } else {
            auditService.audit("action='Check your visa'", "FAILURE");
            LOGGER.error("Unable to find a valid visa.");
        }
        return "visa_refused";
    }

    private void audit(String message, String status, VisaRecord record) {
        auditService.auditForPublicUser(message, status,
                record.firstValueAsStringFor(VisaRuleConstants.FULL_NAME),
                record.firstValueAsStringFor(VisaRuleConstants.EMAIL_ADDRESS),
                record.firstValueAsStringFor(VisaRuleConstants.PASSPORT_NUMBER));
    }

    private Optional<VisaRecord> getVisaRecord(Authentication authentication) {
        return authentication != null ? ofNullable(visaRecordService.get(authentication.getPrincipal().toString())) : empty();
    }

    @RequestMapping(value = "/visa/arrive", method =  RequestMethod.POST)
    public String whenYouArriveUk(final Map<String, Object> model, final Authentication authentication) {
        final Optional<VisaRecord> visaRecord = getVisaRecord(authentication);
        visaRecord.ifPresentOrElse(vr ->
        {
            final Boolean policeRegistrationMessage = vr.getVisaRulesMapping().stream().anyMatch(filter -> filter.get_1().getRule().equals(VisaRuleConstants.POLICE_REGISTRATION_NVN) || filter.get_1().getRule().equals(VisaRuleConstants.POLICE_REGISTRATION_VN));
            if(policeRegistrationMessage){
                model.put("policeRegistrationMessage", POLICE_REGISTERATION_REQUIRED);
            } else {
                model.put("policeRegistrationMessage", POLICE_REGISTERATION_NOT_REQUIRED);
            }
            final String address = vr.getVisaRulesMapping().stream()
                    .filter(filter -> filter.get_1().getRule().equals(VisaRuleConstants.BRP_COLLECTION_INFO))
                    .findFirst().get()
                    .get_2().stream().findFirst().get().getContent();
            model.put("address", address);
            audit("action='When you arrive to UK'", "SUCCESS", visaRecord.get());
        }, () -> {
            LOGGER.error("Unable to find a valid visa.");
            auditService.audit("action='Check your visa'", "FAILURE");
        });
        return "when_you_arrive_in_uk";
    }

    @RequestMapping(value = "/visa/details", method = RequestMethod.POST)
    public String visaTravel(final Map<String, Object> model, final Authentication authentication) {
        getVisaTravel(model, authentication);
        return "visa_travel";
    }

    private void getVisaTravel(Map<String, Object> model, Authentication authentication) {
        final Optional<VisaRecord> visaRecord = getVisaRecord(authentication);
        visaRecord.ifPresentOrElse(vr ->
        {
            final String fromTravelDate = vr.getVisaRulesMapping().stream()
                    .filter(filter -> filter.get_1().getRule().equals(VisaRuleConstants.VALID_FROM))
                    .findFirst().get()
                    .get_2().stream()
                    .findFirst().get().getContent();
            final LocalDate fromDate = fromDisplayDate(fromTravelDate);
            final LocalDate toDate = fromDisplayDate(fromTravelDate).plusDays(MAX_TRAVEL_DAYS);
            model.put("fromTravelDisplayDate", parse(fromDate, DateTimeUtils.DISPLAY_DATE_TIME_PATTERN));
            model.put("toTravelDisplayDate", parse(toDate, DateTimeUtils.DISPLAY_DATE_TIME_PATTERN));
            audit("action='Travel to the UK'", "SUCCESS", visaRecord.get());
        }, () -> {
            auditService.audit("action='Check your visa'", "FAILURE");
            LOGGER.error("Unable to find a valid visa.");
        });
    }

    @RequestMapping(value = "/visa/travel", method = RequestMethod.GET)
    public String visaTravelForward(final Map<String, Object> model, final Authentication authentication) {
        getVisaTravel(model, authentication);
        return "visa_travel";
    }


}
