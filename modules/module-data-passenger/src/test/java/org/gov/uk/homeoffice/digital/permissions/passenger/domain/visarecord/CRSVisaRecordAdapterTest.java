package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CRSVisaRecordAdapter.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(MockitoJUnitRunner.Silent.class)
public class CRSVisaRecordAdapterTest extends VisaRecordAdapterTest {

    @Mock
    private CrsRecordRepository crsRecordRepository;

    private CRSVisaRecordAdapter crsVisaRecordAdapter;

    private final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> expectedCommonRules = Set.of(
            Tuple.tpl(new VisaRule("PLACE_OF_ISSUE"), List.of(new VisaRuleContent(-1L, "PLACE_OF_ISSUE", "place-of-issue", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VALID_FROM"), List.of(new VisaRuleContent(-1L, "VALID_FROM",  "20180610", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VALID_UNTIL"), List.of(new VisaRuleContent(-1L, "VALID_UNTIL",  "20181220", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("WORK_UNTIL"), List.of(new VisaRuleContent(-1L, "WORK_UNTIL",  "", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("NUMBER_OF_ENTRIES"), List.of(new VisaRuleContent(-1L, "NUMBER_OF_ENTRIES", "number-of-entries", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("EMAIL_ADDRESS"), List.of(new VisaRuleContent(-1L, "EMAIL_ADDRESS", "email-address", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("MOBILE_NUMBER"), List.of(new VisaRuleContent(-1L, "MOBILE_NUMBER", "mobile-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VAF_NUMBER"), List.of(new VisaRuleContent(-1L, "VAF_NUMBER", "vaf-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VISA_TYPE"), List.of(new VisaRuleContent(-1L, "VISA_TYPE", "visa-type", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SURNAME"), List.of(new VisaRuleContent(-1L, "SURNAME", "surname", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("NAME"), List.of(new VisaRuleContent(-1L, "NAME", "name surname", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("PASSPORT_NUMBER"), List.of(new VisaRuleContent(-1L, "PASSPORT_NUMBER", "passport-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("GENDER"), List.of(new VisaRuleContent(-1L, "GENDER", "MALE", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("DATE_OF_BIRTH"), List.of(new VisaRuleContent(-1L, "DATE_OF_BIRTH", "19960102", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("NATIONALITY"), List.of(new VisaRuleContent(-1L, "NATIONALITY", "nationality", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("COS_NUMBER"), List.of(new VisaRuleContent(-1L, "COS_NUMBER", "cos-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("CAS_NUMBER"), List.of(new VisaRuleContent(-1L, "CAS_NUMBER", "cas-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SPX_NUMBER"), List.of(new VisaRuleContent(-1L, "SPX_NUMBER",  "spx-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule(VisaRuleConstants.BRP_COLLECTION_INFO), List.of(new VisaRuleContent(-1L, "BRP_COLLECTION_INFO",  "brp-collection-info", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SPONSOR_NAME"), List.of(new VisaRuleContent(-1L, "SPONSOR_NAME",  "sponsor-name", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SPONSOR_TYPE"), List.of(new VisaRuleContent(-1L, "SPONSOR_TYPE",  "sponsor-type", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SPONSOR_ADDRESS"), List.of(new VisaRuleContent(-1L, "SPONSOR_ADDRESS",  "sponsor-address", true, RuleType.USER_DATA)))
    );

    private CrsRecord.CrsRecordBuilder commonPropertiesCrsRecordBuilder = CrsRecord.builder()
            .status(VisaStatus.ISSUED)
            .postName("place-of-issue")
            .validFrom(LocalDate.of(2018, 6, 10))
            .validTo(LocalDate.of(2018, 12, 20))
            .entryType("number-of-entries")
            .emailAddress("email-address")
            .mobileNumber("mobile-number")
            .vafNo("vaf-number")
            .visaEndorsement("visa-type")
            .familyName("surname")
            .otherName("name")
            .passportNumber("passport-number")
            .gender(Gender.MALE)
            .dateOfBirth(LocalDate.of(1996, 1, 2))
            .nationality("nationality")
            .brpCollectionInfo("brp-collection-info")
            .cosNo("cos-number")
            .casNo("cas-number")
            .sponsorName("sponsor-name")
            .sponsorType("sponsor-type")
            .sponsorAddress("sponsor-address")
            .sponsorSpxNo("spx-number");

    @Before
        public void setUp() { ;
        initVisaRuleLookupRepositoryMock();
        crsVisaRecordAdapter = new CRSVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository, crsRecordRepository);
    }

    @Test
    public void visaRecordWithNoEndorsements() {
        CrsRecord crsRecord = commonPropertiesCrsRecordBuilder.build();

        VisaRecord visaRecord = crsVisaRecordAdapter.getVisaRecord(crsRecord);

        assertThat(visaRecord, is(new VisaRecord(
                VisaStatus.ISSUED,
                VisaType.createVisaType("visa-type"),
                expectedCommonRules)));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1() {
        testCatDEndorsement(CODE_1_ENDORSEMENT, editableTuple("CODE_1", "code1"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1A() {
        testCatDEndorsement(CODE_1A_ENDORSEMENT, editableTuple("CODE_1A", "code1a"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_2() {
        testCatDEndorsement(CODE_2_ENDORSEMENT, editableTuple("CODE_2", "code2"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_3() {
        testCatDEndorsement(CODE_3_ENDORSEMENT, editableTuple("CODE_3", "code3"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_4() {
        testCatDEndorsement(CODE_4_ENDORSEMENT, editableTuple("CODE_4", "code4"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_7() {
        testCatDEndorsement(CODE_7_ENDORSEMENT, editableTuple("CODE_7", "code7"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_10_HOUR() {
        testCatDEndorsement(TEN_HOUR_WORKING_ENDORSEMENT, editableTuple("10_HOURS", "10-hours"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_20_HOUR() {
        testCatDEndorsement(TWENTY_HOUR_WORKING_ENDORSEMENT, editableTuple("20_HOURS", "20-hours"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_SPORTS() {
        testCatDEndorsement(SPORTS_ENDORSEMENT, editableTuple("SPORTS", "sports"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_DOCTOR() {
        testCatDEndorsement(DOCTOR_ENDORSEMENT, editableTuple("DOCTOR", "doctor"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_BUSINESS() {
        testCatDEndorsement(BUSINESS_ENDORSEMENT, editableTuple("BUSINESS", "business"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1_and_10_HOURS() {
        testCatDEndorsement(CODE_1_ENDORSEMENT,
                TEN_HOUR_WORKING_ENDORSEMENT,
                editableTuple("CODE_1", "code1"),
                editableTuple("10_HOURS", "10-hours"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1_and_20_HOURS_and_POLICE_REGISTRATION() {
        testCatDEndorsement(CODE_1_ENDORSEMENT,
                TEN_HOUR_WORKING_ENDORSEMENT + POLICE_REGISTRATION,
                editableTuple("CODE_1", "code1"),
                editableTuple("10_HOURS", "10-hours"),
                nonEditableTuple("POLICE_REGISTRATION_NVN", "Police registration"));
    }

    private void testCatDEndorsement(String catDEndorsement, Tuple<VisaRule, Collection<VisaRuleContent>> tuple) {
        CrsRecord crsRecord = commonPropertiesCrsRecordBuilder
                .catDEndors1(catDEndorsement)
                .build();

        VisaRecord visaRecord = crsVisaRecordAdapter.getVisaRecord(crsRecord);

        assertThat(visaRecord, is(new VisaRecord(
                VisaStatus.ISSUED,
                VisaType.createVisaType("visa-type"),
                merge(expectedCommonRules, tuple))));
    }

    private void testCatDEndorsement(
            String catDEndorsement1,
            String catDEndorsement2,
            Tuple<VisaRule, Collection<VisaRuleContent>> ...tuples) {
        CrsRecord crsRecord = commonPropertiesCrsRecordBuilder
                .catDEndors1(catDEndorsement1)
                .catDEndors2(catDEndorsement2)
                .build();

        VisaRecord visaRecord = crsVisaRecordAdapter.getVisaRecord(crsRecord);

        assertThat(visaRecord, is(new VisaRecord(
                VisaStatus.ISSUED,
                VisaType.createVisaType("visa-type"),
                merge(expectedCommonRules, tuples))));
    }

}