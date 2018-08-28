package org.gov.uk.homeoffice.digital.permissions.passenger.visa.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.gov.uk.homeoffice.digital.permissions.passenger.visa.rule.DynamicContentProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisaControllerTest {

    public static final String VALID_FROM = "20180610";
    public static final String VALID_TO = "20180610";
    public static final String ADDRESS = "Address, UC1 2AB";

    private final String defaultVisaEndorsement = "default-visa-endorsement";

    private final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> expectedCommonRules = Set.of(
            Tuple.tpl(new VisaRule("VISA_TYPE"), toVisaRuleContents("VISA_TYPE", defaultVisaEndorsement)),
            Tuple.tpl(new VisaRule("VAF_NUMBER"),  toVisaRuleContents("VAF_NUMBER","vaf-number")),
            Tuple.tpl(new VisaRule("CAS_NUMBER"),  toVisaRuleContents("CAS_NUMBER","cas-number")),
            Tuple.tpl(new VisaRule("SURNAME"),  toVisaRuleContents("SURNAME", "surname")),
            Tuple.tpl(new VisaRule("NAME"),  toVisaRuleContents("NAME", "first-name middle-name surname")),
            Tuple.tpl(new VisaRule("DATE_OF_BIRTH"),  toVisaRuleContents("DATE_OF_BIRTH", "19960102")),
            Tuple.tpl(new VisaRule("NATIONALITY"),  toVisaRuleContents("NATIONALITY", "nationality")),
            Tuple.tpl(new VisaRule("PASSPORT_NUMBER"),  toVisaRuleContents("PASSPORT_NUMBER", "passport-number")),
            Tuple.tpl(new VisaRule("VALID_FROM"),  toVisaRuleContents("VALID_FROM", VALID_FROM)),
            Tuple.tpl(new VisaRule("VALID_UNTIL"),  toVisaRuleContents("VALID_UNTIL", "20181220")),
            Tuple.tpl(new VisaRule("SPX_NUMBER"),  toVisaRuleContents("SPX_NUMBER", "spx-number")),
            Tuple.tpl(new VisaRule("REASON"),  toVisaRuleContents("REASON", "reason")),
            Tuple.tpl(new VisaRule("BRP_COLLECTION_INFO"),  toVisaRuleContents("BRP_COLLECTION_INFO", ADDRESS))
    );
    private final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> expectedRulesWithPoliceRegistration = Set.of(
            Tuple.tpl(new VisaRule("VISA_TYPE"), toVisaRuleContents("VISA_TYPE", defaultVisaEndorsement)),
            Tuple.tpl(new VisaRule("VAF_NUMBER"),  toVisaRuleContents("VAF_NUMBER","vaf-number")),
            Tuple.tpl(new VisaRule("CAS_NUMBER"),  toVisaRuleContents("CAS_NUMBER","cas-number")),
            Tuple.tpl(new VisaRule("SURNAME"),  toVisaRuleContents("SURNAME", "surname")),
            Tuple.tpl(new VisaRule("NAME"),  toVisaRuleContents("NAME", "first-name middle-name surname")),
            Tuple.tpl(new VisaRule("DATE_OF_BIRTH"),  toVisaRuleContents("DATE_OF_BIRTH", "19960102")),
            Tuple.tpl(new VisaRule("NATIONALITY"),  toVisaRuleContents("NATIONALITY", "nationality")),
            Tuple.tpl(new VisaRule("PASSPORT_NUMBER"),  toVisaRuleContents("PASSPORT_NUMBER", "passport-number")),
            Tuple.tpl(new VisaRule("VALID_FROM"),  toVisaRuleContents("VALID_FROM", VALID_FROM)),
            Tuple.tpl(new VisaRule("VALID_UNTIL"),  toVisaRuleContents("VALID_UNTIL", "20181220")),
            Tuple.tpl(new VisaRule("SPX_NUMBER"),  toVisaRuleContents("SPX_NUMBER", "spx-number")),
            Tuple.tpl(new VisaRule("REASON"),  toVisaRuleContents("REASON", "reason")),
            Tuple.tpl(new VisaRule("POLICE_REGISTRATION_VN"), toVisaRuleContents("POLICE_REGISTRATION_VN", "reason")),
            Tuple.tpl(new VisaRule("BRP_COLLECTION_INFO"),  toVisaRuleContents("BRP_COLLECTION_INFO", ADDRESS))
    );

    @Mock
    private VisaTypeService mockVisaTypeService;

    @Mock
    private VisaRecordService mockVisaRecordService;

    @Mock
    private DynamicContentProcessor dynamicContentProcessor;

    @Mock
    private Principal mockPrincipal;

    @Mock
    Authentication mockAuthentication;

    Map<String, Object> model = new HashMap<>();
    final Optional<VisaTypeRule> visaTypeRuleOpt = Optional.of(new VisaTypeRule(VisaType.createVisaType("test"), Collections.emptyList()));


    private VisaController testObject;

    @Before
    public void setUp() throws Exception {
        when(mockAuthentication.getPrincipal()).thenReturn(mockPrincipal);
        testObject = new VisaController(mockVisaTypeService, mockVisaRecordService, dynamicContentProcessor);

    }

    private static final String PASSPORT_NUMBER = "887665237";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1988, 11, 26);

    @Test
    public void getVisaStatus(){
        VisaRecord visaRecord = new VisaRecord(
                VisaStatus.VALID,
                VisaType.createVisaType(defaultVisaEndorsement),
                expectedCommonRules);

        when(mockVisaRecordService.get(mockPrincipal.toString())).thenReturn(visaRecord);
        when(mockVisaTypeService.findVisaTypeRule(visaRecord)).thenReturn(Tuple.tpl(visaTypeRuleOpt, Collections.emptyList()));

        testObject.visaDetails(model, mockAuthentication);

        assertThat(model.get("visa"), is(notNullValue()));
    }

    @Test
    public void getVisaStatusWhenVisaNotFound(){

        when(mockVisaRecordService.get(mockPrincipal.toString())).thenReturn(null);

        testObject.visaDetails(model, mockAuthentication);

        assertThat(model.get("visa"), is(nullValue()));
    }

    @Test
    public void getVisaTravelDates(){
        VisaRecord visaRecord = new VisaRecord(
                VisaStatus.VALID,
                VisaType.createVisaType(defaultVisaEndorsement),
                expectedCommonRules);

        when(mockVisaRecordService.get(mockPrincipal.toString())).thenReturn(visaRecord);

        testObject.visaTravel(model, mockAuthentication);

        assertThat(model.get("fromTravelDisplayDate"), is(equalTo("Sunday, 10 Jun 2018")));
        assertThat(model.get("toTravelDisplayDate"), is(equalTo("Tuesday, 10 Jul 2018")));
    }

    @Test
    public void getWhenYouArriveInUk(){
        VisaRecord visaRecord = new VisaRecord(
                VisaStatus.VALID,
                VisaType.createVisaType(defaultVisaEndorsement),
                expectedCommonRules);

        when(mockVisaRecordService.get(mockPrincipal.toString())).thenReturn(visaRecord);

        testObject.whenYouArriveUk(model, mockAuthentication);

        assertThat(model.get("policeRegistrationMessage"), is(equalTo(VisaController.POLICE_REGISTERATION_NOT_REQUIRED)));
        assertThat(model.get("address"), is(equalTo(ADDRESS)));
    }

    @Test
    public void expectedRulesWithPoliceRegistration(){
        VisaRecord visaRecord = new VisaRecord(
                VisaStatus.VALID,
                VisaType.createVisaType(defaultVisaEndorsement),
                expectedRulesWithPoliceRegistration);

        when(mockVisaRecordService.get(mockPrincipal.toString())).thenReturn(visaRecord);

        testObject.whenYouArriveUk(model, mockAuthentication);

        assertThat(model.get("policeRegistrationMessage"), is(equalTo(VisaController.POLICE_REGISTERATION_REQUIRED)));
        assertThat(model.get("address"), is(equalTo(ADDRESS)));
    }

    private List<VisaRuleContent> toVisaRuleContents(String rule, String value) {
        return List.of(new VisaRuleContent(-1L, rule, value, true, RuleType.USER_DATA));
    }

}
