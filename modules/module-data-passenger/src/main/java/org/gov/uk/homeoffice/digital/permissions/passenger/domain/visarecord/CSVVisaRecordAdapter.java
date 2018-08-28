package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleContentRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.toDisplayDate;

@Component
public class CSVVisaRecordAdapter extends AbstractVisaRecordAdapter {

    public static final String TEN_HOUR_WORKING_ENDORSEMENT = "T4 G Student SPX Work limit 10 hrs, p/w term time.";
    public static final String TWENTY_HOUR_WORKING_ENDORSEMENT = "You can work for up to 20 hours a week during term time";

    public static final String BUSINESS_ENDORSEMENT = "On your current visa you cannot engage in business activity, such as being self-employed or running a business";
    public static final String SPORTS_ENDORSEMENT = "On your current visa, you cannot play or coach professional sports";
    public static final String DOCTOR_ENDORSEMENT = "On your current visa you cannot work as a doctor or dentist in training";

    public static final String CODE_1 = "You can't get public funds, for example housing benefit";
    public static final String CODE_1A = "On your current visa you have limited leave to enter the UK";
    public static final String CODE_2 = "On your current visa if you work in the UK this must be authorised (and any changes) must be authorised";
    public static final String CODE_3 = "On your current visa you cannot work or recourse to public funds";
    public static final String CODE_4 = "On your current visa you cannot get public funds. You can work with * changes must be authorised * name or organisation/employer: add endorsement field";
    public static final String CODE_7 = "On your current visa you cannot get public funds. You must leave the UK by *";

    private VisaRepository visaRepository;
    private ParticipantRepository participantRepository;
    private String defaultVisaEndorsement;

    @Autowired
    public CSVVisaRecordAdapter(final VisaRepository visaRepository,
                                final ParticipantRepository participantRepository,
                                final VisaRuleLookupRepository visaRuleLookupRepository,
                                final VisaRuleContentRepository visaRuleContentRepository,
                                @Value("${visa.default-endorsement}") final String defaultVisaEndorsement) {
        super(visaRuleLookupRepository, visaRuleContentRepository);
        this.visaRepository = visaRepository;
        this.participantRepository = participantRepository;
        this.defaultVisaEndorsement = defaultVisaEndorsement;
    }

    @Override
    public VisaAdapterType getType() {
        return VisaAdapterType.CSV;
    }

    @Override
    public VisaRecord toVisaRecord(final String id) {

        final Participant participant = participantRepository.getById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        final Visa visa = visaRepository.getByPassportNumber(participant.getPassportNumber()).orElseThrow(() -> new IllegalArgumentException("Visa not found"));

        return getVisaRecord(participant, visa);
    }

    public VisaRecord getVisaRecord(final Participant participant, final Visa visa) {
        final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> visaRuleMappings = new HashSet<>();

        visaRuleMappings.add(ruleFor(VisaRuleConstants.VISA_TYPE, defaultVisaEndorsement));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.VAF_NUMBER, participant.getVaf()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.CAS_NUMBER, participant.getCas()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SURNAME, participant.getSurName()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.FULL_NAME, getName(participant)));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.DATE_OF_BIRTH, toDisplayDate(participant.getDateOfBirth())));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.NATIONALITY, participant.getNationality()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.PASSPORT_NUMBER, participant.getPassportNumber()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.VALID_FROM, toDisplayDate(visa.getValidFrom())));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.VALID_UNTIL, toDisplayDate(visa.getValidTo())));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SPX_NUMBER, visa.getSpx()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.POLICE_REGISTRATION_VN, ""));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.REASON, visa.getReason()));

        if (hasEndorsement(TEN_HOUR_WORKING_ENDORSEMENT, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.WORKING_10_HOURS));
        }
        if (hasEndorsement(TWENTY_HOUR_WORKING_ENDORSEMENT, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.WORKING_20_HOURS));
        }
        if (hasEndorsement(CODE_1, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_1));
        }
        if (hasEndorsement(CODE_1A, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_1A));
        }
        if (hasEndorsement(CODE_2, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_2));
        }
        if (hasEndorsement(CODE_3, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_3));
        }
        if (hasEndorsement(CODE_4, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_4));
        }
        if (hasEndorsement(CODE_7, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_7));
        }
        if (hasEndorsement(SPORTS_ENDORSEMENT, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.SPORTS));
        }
        if (hasEndorsement(BUSINESS_ENDORSEMENT, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.BUSINESS));
        }
        if (hasEndorsement(DOCTOR_ENDORSEMENT, visa)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.DOCTOR));
        }

        return new VisaRecord(visa.getStatus(), VisaType.createVisaType(defaultVisaEndorsement), visaRuleMappings);
    }

    @Override
    public Optional<String> getIdentifier(String passportNumber, LocalDate dateOfBirth) {
        Optional<Participant> participant = participantRepository.getByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth);
        if (participant.isPresent() && visaRepository.getByPassportNumber(passportNumber).isPresent()){
            return of(String.valueOf(participant.get().getId()));
        }
        return empty();
    }

    @Override
    public Collection<VisaRecord> getValidWithinRange(LocalDate lowerValidDate, LocalDate upperValidDate) {
        return participantRepository.getByVisaValidFrom(lowerValidDate, upperValidDate)
                .stream()
                .map(participant -> String.valueOf(participant.getId()))
                .map(this::toVisaRecord)
                .collect(Collectors.toList());
    }

    private String getName(final Participant participant) {
        final String nameBuilder = ofNullable(participant.getFirstName()).orElse("") + " " +
                ofNullable(participant.getMiddleName()).orElse("") + " " +
                ofNullable(participant.getSurName()).orElse("");
        return nameBuilder.trim().replaceAll("\\p{Space}{2,}?", " ");
    }

    private boolean hasEndorsement(String endorsement, Visa visa) {
        return visa.getCatDEndorsements().stream().anyMatch(e -> e.contains(endorsement));
    }

}
