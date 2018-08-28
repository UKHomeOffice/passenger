package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.ParseError;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleMatcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CSVVisaRecordAdapter;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;

@Component
public class ParticipantsParser {

    @Autowired
    private VisaRuleMatcher visaRuleMatcher;

    @Autowired
    private CSVVisaRecordAdapter csvVisaRecordAdapter;

    public ParsedResult parse(File tempFile) {
        return Catcher.convertToRuntime(() -> {
            final List<String> lines = FileUtils.readLines(tempFile, "UTF-8");
            final Map<CSVField, List<Integer>> fieldIndices = fieldIndices(lines.get(0));

            final List<Tuple<Optional<Tuple<Participant, Visa>>, Optional<ParseError>>> tuples = lines.stream()
                    .skip(1)
                    .map(row -> parseRow(row, fieldIndices))
                    .collect(toList());

            return new ParsedResult(
                    tuples.stream().filter(tpl -> tpl.get_1().isPresent()).map(tpl -> tpl.get_1().get()).collect(toList()),
                    tuples.stream().filter(tpl1 -> tpl1.get_2().isPresent()).map(tpl1 -> tpl1.get_2().get()).collect(toList())
            );
        });

    }

    private Map<CSVField, List<Integer>> fieldIndices(String header) {
        return Catcher.convertToRuntime(() -> {
            final CSVRecord csvRecord = CSVParser.parse(header, CSVFormat.DEFAULT).getRecords().get(0);
            return Arrays.stream(CSVField.values()).map(field ->
                    tpl(field, indexOfField(csvRecord, field)))
                    .collect(toMap(tpl -> tpl.get_1(), tpl -> tpl.get_2()));
        });

    }

    private List<Integer> indexOfField(CSVRecord csvRecord, CSVField field) {
        return IntStream.range(0, csvRecord.size())
                .filter(index -> csvRecord.get(index).trim().equalsIgnoreCase(field.description)).mapToObj(index -> index).collect(toList());
    }

    /**
     * For stage 1, visa information is extracted from the spreadsheet,and there is no concept of a  separate entry clearance.
     * When we reach stage 2, the code below will need to change as the Visa information below will instead come from a SEDI getFile.
     * Also, for stage 2, there will be a concept of an entry clearance with it's own validity dates.
     **/
    private Tuple<Optional<Tuple<Participant, Visa>>, Optional<ParseError>> parseRow(String row, Map<CSVField, List<Integer>> fieldIndices) {
        try {
            final CSVRecord csvRecord = CSVParser.parse(row, CSVFormat.DEFAULT).getRecords().get(0);
            final Visa visa = new VisaBuilder()
                    .setPassportNumber(fieldValue(fieldIndices, csvRecord, CSVField.PASSPORT, val -> val.get(0).trim()))
                    .setValidFrom(fieldValue(fieldIndices, csvRecord, CSVField.VALID_FROM, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .setValidTo(fieldValue(fieldIndices, csvRecord, CSVField.VALID_UNTIL, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .setSpx(fieldValue(fieldIndices, csvRecord, CSVField.SPX, val -> val.get(0).trim()))
                    .setCatDEndorsements(fieldValue(fieldIndices, csvRecord, CSVField.CAT_D_ENDORSEMENT, (val) -> val.stream().map(str -> str.trim()).collect(toList())))
                    .setStatus(fieldValue(fieldIndices, csvRecord, CSVField.ACTION, val -> VisaStatus.parse(val.get(0)), VisaStatus.VALID))
                    .setReason(fieldValue(fieldIndices, csvRecord, CSVField.REASON, val -> val.get(0).trim()))
                    .createVisa();

            final Participant participant = new ParticipantBuilder()
                    .setId(fieldValue(fieldIndices, csvRecord, CSVField.ID, val -> Long.parseLong(val.get(0))))
                    .setGwf(fieldValue(fieldIndices, csvRecord, CSVField.GWF, val -> val.get(0).trim()))
                    .setVaf(fieldValue(fieldIndices, csvRecord, CSVField.VAF, val -> val.get(0).trim()))
                    .setCas(fieldValue(fieldIndices, csvRecord, CSVField.CAS, val -> val.get(0).trim()))
                    .setFirstName(fieldValue(fieldIndices, csvRecord, CSVField.FIRST_NAME, val -> capitalizeFully(val.get(0)).trim()))
                    .setMiddleName(fieldValue(fieldIndices, csvRecord, CSVField.MIDDLE_NAME, val -> capitalizeFully(val.get(0)).trim()))
                    .setSurName(fieldValue(fieldIndices, csvRecord, CSVField.SURNAME, val -> capitalizeFully(val.get(0)).trim()))
                    .setDateOfBirth(fieldValue(fieldIndices, csvRecord, CSVField.DATE_OF_BIRTH, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .setNationality(fieldValue(fieldIndices, csvRecord, CSVField.NATIONALITY, val -> val.get(0).trim()))
                    .setPassportNumber(fieldValue(fieldIndices, csvRecord, CSVField.PASSPORT, val -> val.get(0).trim()))
                    .setMobileNumber(fieldValue(fieldIndices, csvRecord, CSVField.MOBILE, val -> val.get(0).trim()))
                    .setEmail(fieldValue(fieldIndices, csvRecord, CSVField.EMAIL, val -> val.get(0).trim()))
                    .setInstitutionAddress(fieldValue(fieldIndices, csvRecord, CSVField.INSTITUTION_ADDRESS, val -> val.get(0).trim()))
                    .createParticipant();

            final VisaRecord visaRecord = csvVisaRecordAdapter.getVisaRecord(participant, visa);
            if (visaRuleMatcher.hasVisaRule(visaRecord, (visaRules) -> {})) {
                return tpl(Optional.of(tpl(participant, visa)), empty());
            }
            else {
                final StringBuilder errorMessage = new StringBuilder();
                visaRecord.getVisaRulesMapping().forEach(tpl -> {
                    errorMessage.append(tpl.get_1().getRule());
                    errorMessage.append(": ");
                    errorMessage.append(tpl.get_2());
                    errorMessage.append(", ");
                });
                return new Tuple<>(empty(), Optional.of(new ParseError(row, new IllegalArgumentException("Business rule cannot be matched. " + errorMessage.toString()))));
            }
        } catch (Exception e) {
            return new Tuple<>(empty(), Optional.of(new ParseError(row, e)));
        }
    }

    private <T> T fieldValue(Map<CSVField, List<Integer>> fieldIndices, CSVRecord csvRecord, CSVField field, Function<List<String>, T> mapper) {
        return fieldValue(fieldIndices, csvRecord, field, mapper, null);
    }

    private <T> T fieldValue(Map<CSVField, List<Integer>> fieldIndices, CSVRecord csvRecord, CSVField field, Function<List<String>, T> mapper, T defaultValue) {
        final List<Integer> indices = fieldIndices.get(field);
        if (indices.isEmpty()) return defaultValue;
        final List<String> collect = indices.stream().map(index -> valueForIndex(csvRecord, index)).filter(val -> val.isPresent()).map(val -> val.get()).collect(toList());
        return collect.isEmpty() ? defaultValue : mapper.apply(collect);
    }

    private Optional<String> valueForIndex(CSVRecord row, int index) {
        return Optional.ofNullable(row.get(index)).flatMap(val -> StringUtils.isNotBlank(val) ? Optional.of(val) : empty());
    }
}
