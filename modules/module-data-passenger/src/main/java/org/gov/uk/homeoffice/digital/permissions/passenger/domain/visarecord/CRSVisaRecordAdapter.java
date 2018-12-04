package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleContentRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.toDisplayDate;

@Component
public class CRSVisaRecordAdapter extends AbstractVisaRecordAdapter {

    public static final String TEN_HOUR_WORKING_ENDORSEMENT = "Work limit 10 hr";
    public static final String TWENTY_HOUR_WORKING_ENDORSEMENT = "Work limit 20 hr";

    public static final String BUSINESS_ENDORSEMENT = "On your current visa you cannot engage in business activity, such as being self-employed or running a business";
    public static final String SPORTS_ENDORSEMENT = "On your current visa, you cannot play or coach professional sports";
    public static final String DOCTOR_ENDORSEMENT = "On your current visa you cannot work as a doctor or dentist in training";

    public static final String POLICE_REGISTRATION = "Police registration";

    public static final String CODE_1_ENDORSEMENT = "No recourse to public funds";
    public static final String CODE_1A_ENDORSEMENT = "Limited leave to enter";
    public static final String CODE_2_ENDORSEMENT = "Work (and any changes) must be authorised";
    public static final String CODE_3_ENDORSEMENT = "No work or recourse to public funds";
    public static final String CODE_4_ENDORSEMENT = "changes must be authorised";
    public static final String CODE_7_ENDORSEMENT = "Must leave in";

    private final CrsRecordRepository crsRecordRepositoryBean;

    public CRSVisaRecordAdapter(VisaRuleLookupRepository visaRuleLookupRepository,
                                VisaRuleContentRepository visaRuleContentRepository,
                                CrsRecordRepository crsRecordRepositoryBean) {
        super(visaRuleLookupRepository, visaRuleContentRepository);
        this.crsRecordRepositoryBean = crsRecordRepositoryBean;
    }

    @Override
    public VisaAdapterType getType() {
        return VisaAdapterType.CRS;
    }

    @Override
    public VisaRecord toVisaRecord(String crsIdentifier) {
        CrsRecord crsRecord = crsRecordRepositoryBean.getById(Long.valueOf(crsIdentifier))
                .orElseThrow(() -> new IllegalArgumentException("No record found for " + crsIdentifier));

        return getVisaRecord(crsRecord);
    }

    public VisaRecord getVisaRecord(CrsRecord crsRecord) {
        final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> visaRuleMappings = new LinkedHashSet<>();

        visaRuleMappings.add(ruleFor(VisaRuleConstants.VISA_TYPE, crsRecord.getVisaEndorsement()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.VAF_NUMBER, crsRecord.getVafNo()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.CAS_NUMBER, crsRecord.getCasNo()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SURNAME, crsRecord.getFamilyName()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.FULL_NAME, getName(crsRecord)));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.DATE_OF_BIRTH, toDisplayDate(crsRecord.getDateOfBirth())));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.NATIONALITY, crsRecord.getNationality()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.PASSPORT_NUMBER, crsRecord.getPassportNumber()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.VALID_FROM, toDisplayDate(crsRecord.getValidFrom())));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.VALID_UNTIL, toDisplayDate(crsRecord.getValidTo())));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SPX_NUMBER, crsRecord.getSponsorSpxNo()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SPONSOR_NAME, crsRecord.getSponsorName()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SPONSOR_TYPE, crsRecord.getSponsorType()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.SPONSOR_ADDRESS, crsRecord.getSponsorAddress()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.PLACE_OF_ISSUE, crsRecord.getPostName()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.NUMBER_OF_ENTRIES, crsRecord.getEntryType()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.COS_NUMBER, crsRecord.getCosNo()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.MOBILE_NUMBER, crsRecord.getMobileNumber()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.EMAIL_ADDRESS, crsRecord.getEmailAddress()));
        visaRuleMappings.add(ruleFor(VisaRuleConstants.BRP_COLLECTION_INFO, crsRecord.getBrpCollectionInfo()));

        if(crsRecord.getGender() != null){
            visaRuleMappings.add(ruleFor(VisaRuleConstants.GENDER, crsRecord.getGender().name()));
        }

        String catDEndorsements = crsRecord.getCatDEndors1() + crsRecord.getCatDEndors2();

        if (hasString(catDEndorsements, CODE_1_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_1));
        }
        if (hasString(catDEndorsements, CODE_1A_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_1A));
        }
        if (hasString(catDEndorsements, CODE_2_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_2));
        }
        if (hasString(catDEndorsements, CODE_3_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_3));
        }

        if (hasString(catDEndorsements, CODE_4_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_4));
        }

        if (hasString(catDEndorsements, CODE_7_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.CODE_7));
        }

        String additionalEndorsement =
                Optional.ofNullable(crsRecord.getAdditionalEndorsement1()).orElse("") +
                        Optional.ofNullable(crsRecord.getAdditionalEndorsement2()).orElse("");

        if (!isEmpty(additionalEndorsement)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.ADDITIONAL_ENDORSEMENT, "Must leave in..."));
        }

        if (hasString(catDEndorsements, TEN_HOUR_WORKING_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.WORKING_10_HOURS));
        }

        if (hasString(catDEndorsements, TWENTY_HOUR_WORKING_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.WORKING_20_HOURS));
        }

        if (hasString(catDEndorsements, TWENTY_HOUR_WORKING_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.WORKING_20_HOURS));
        }

        if (hasString(catDEndorsements, BUSINESS_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.BUSINESS));
        }

        if (hasString(catDEndorsements, SPORTS_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.SPORTS));
        }

        if (hasString(catDEndorsements, DOCTOR_ENDORSEMENT)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.DOCTOR));
        }

        if (hasString(catDEndorsements, POLICE_REGISTRATION)) {
            visaRuleMappings.add(ruleFor(VisaRuleConstants.POLICE_REGISTRATION_NVN, "Police registration"));
        }

        return new VisaRecord(crsRecord.getStatus(), VisaType.createVisaType(crsRecord.getVisaEndorsement()), visaRuleMappings);
    }

    @Override
    public Optional<String> getIdentifier(String passportNumber, LocalDate dateOfBirth) {
        return crsRecordRepositoryBean.getByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth)
                .map(v -> Long.toString(v));
    }

    @Override
    public Collection<VisaRecord> getValidWithinRange(LocalDate lowerValidDate, LocalDate upperValidDate) {
        return crsRecordRepositoryBean.getValidWithinRange(lowerValidDate, upperValidDate)
                .stream()
                .map(this::getVisaRecord)
                .collect(Collectors.toList());
    }

    private String getName(final CrsRecord crsRecord) {
        final String nameBuilder = ofNullable(crsRecord.getOtherName()).orElse("") + " " +
                ofNullable(crsRecord.getFamilyName()).orElse("");
        return nameBuilder.trim().replaceAll("\\p{Space}{2,}?", " ");
    }

    private boolean hasString(String container, String string) {
        return container != null && container.toLowerCase().contains(string.toLowerCase());
    }


}
