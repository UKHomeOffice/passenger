package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CSVVisaRecordAdapter.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CSVVisaRecordAdapterTest extends VisaRecordAdapterTest {
    @Mock
    private VisaRepository visaRepository;
    @Mock
    private ParticipantRepository participantRepository;

    private CSVVisaRecordAdapter csvVisaRecordAdapter;

    private final String defaultVisaEndorsement = "default-visa-endorsement";

    private final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> expectedCommonRules = Set.of(
            Tuple.tpl(new VisaRule("VISA_TYPE"), List.of(new VisaRuleContent(-1L, "VISA_TYPE", defaultVisaEndorsement, true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VAF_NUMBER"), List.of(new VisaRuleContent(-1L, "VAF_NUMBER", "vaf-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("CAS_NUMBER"), List.of(new VisaRuleContent(-1L, "CAS_NUMBER", "cas-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SURNAME"), List.of(new VisaRuleContent(-1L, "SURNAME", "surname", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("NAME"), List.of(new VisaRuleContent(-1L, "NAME", "first-name middle-name surname", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("DATE_OF_BIRTH"), List.of(new VisaRuleContent(-1L, "DATE_OF_BIRTH", "19960102", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("NATIONALITY"), List.of(new VisaRuleContent(-1L, "NATIONALITY", "nationality", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("PASSPORT_NUMBER"), List.of(new VisaRuleContent(-1L, "PASSPORT_NUMBER", "passport-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VALID_FROM"), List.of(new VisaRuleContent(-1L, "VALID_FROM",  "20180610", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("VALID_UNTIL"), List.of(new VisaRuleContent(-1L, "VALID_UNTIL",  "20181220", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("SPX_NUMBER"), List.of(new VisaRuleContent(-1L, "SPX_NUMBER",  "spx-number", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("POLICE_REGISTRATION_VN"), List.of(new VisaRuleContent(-1L, "POLICE_REGISTRATION_VN",  "", true, RuleType.USER_DATA))),
            Tuple.tpl(new VisaRule("REASON"), List.of(new VisaRuleContent(-1L, "REASON",  "reason", true, RuleType.USER_DATA)))
    );

    private Participant participant;
    private Visa visa;

    @Before
    public void setUp() {
        initVisaRuleLookupRepositoryMock();
        csvVisaRecordAdapter = new CSVVisaRecordAdapter(visaRepository, participantRepository, visaRuleLookupRepository, visaRuleContentRepository, defaultVisaEndorsement);

        participant = new Participant(1l, "gwf",
                "vaf-number", "cas-number",
                "first-name", "middle-name", "surname",
                LocalDate.of(1996, 1, 2),
                "nationality",
                "passport-number",
                "mobile-number",
                "email",
                "institution-address",
                Set.of(),
                "updated-by",
                LocalDateTime.of(2018, 6, 10, 1, 2, 34),
                LocalDateTime.of(2018, 6, 10, 5, 6, 54));

        visa = new Visa(2L, "passport-number",
                LocalDate.of(2018, 6, 10),
                LocalDate.of(2018, 12, 20),
                "spx-number",
                List.of(),
                VisaStatus.VALID,
                "reason");
    }

    @Test
    public void visaRecordWithNoEndorsements() {
        VisaRecord visaRecord = csvVisaRecordAdapter.getVisaRecord(participant, visa);

        assertThat(visaRecord, is(new VisaRecord(
                VisaStatus.VALID,
                VisaType.createVisaType(defaultVisaEndorsement),
                expectedCommonRules)));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1() {
        testCatDEndorsement(List.of(CODE_1),
                editableTuple("CODE_1", "code1"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1A() {
        testCatDEndorsement(List.of(CODE_1A),
                editableTuple("CODE_1A", "code1a"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_2() {
        testCatDEndorsement(List.of(CODE_2),
                editableTuple("CODE_2", "code2"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_3() {
        testCatDEndorsement(List.of(CODE_3),
                editableTuple("CODE_3", "code3"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_4() {
        testCatDEndorsement(List.of(CODE_4),
                editableTuple("CODE_4", "code4"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_7() {
        testCatDEndorsement(List.of(CODE_7),
                editableTuple("CODE_7", "code7"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_10_HOUR() {
        testCatDEndorsement(List.of(TEN_HOUR_WORKING_ENDORSEMENT),
                editableTuple("10_HOURS", "10-hours"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_20_HOUR() {
        testCatDEndorsement(List.of(TWENTY_HOUR_WORKING_ENDORSEMENT),
                editableTuple("20_HOURS", "20-hours"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_SPORTS() {
        testCatDEndorsement(List.of(SPORTS_ENDORSEMENT),
                editableTuple("SPORTS", "sports"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_BUSINESS_ENDORSEMENT() {
        testCatDEndorsement(List.of(BUSINESS_ENDORSEMENT),
                editableTuple("BUSINESS", "business"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_DOCTOR_ENDORSEMENT() {
        testCatDEndorsement(List.of(DOCTOR_ENDORSEMENT),
                editableTuple("DOCTOR", "doctor"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1_and_10_HOURS() {
        testCatDEndorsement(List.of(CODE_1, TEN_HOUR_WORKING_ENDORSEMENT),
                editableTuple("CODE_1", "code1"),
                editableTuple("10_HOURS", "10-hours"));
    }

    @Test
    public void visaRecordWithCatDEndorsements_CODE_1_and_20_HOURS_and_POLICE_REGISTRATION() {
        testCatDEndorsement(List.of(CODE_1,
                TEN_HOUR_WORKING_ENDORSEMENT + "Police registration"),
                editableTuple("CODE_1", "code1"),
                editableTuple("10_HOURS", "10-hours"),
                nonEditableTuple("POLICE_REGISTRATION_VN", ""));
    }

    private void testCatDEndorsement(
            List<String> catDEndorsements,
            Tuple<VisaRule, Collection<VisaRuleContent>> ...tuples) {

        Visa visaWithEndorsements = visa.withCatDEndorsements(catDEndorsements);
        VisaRecord visaRecord = csvVisaRecordAdapter.getVisaRecord(participant, visaWithEndorsements);

        assertThat(visaRecord, is(new VisaRecord(
                VisaStatus.VALID,
                VisaType.createVisaType(defaultVisaEndorsement),
                merge(expectedCommonRules, tuples))));
    }

}