package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Gender;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;

@Component
public class CrsFileParser {

    public CrsParsedResult parse(File tempFile) {
        return Catcher.convertToRuntime(() -> {
            final List<String> lines = FileUtils.readLines(tempFile, "UTF-8");
            final Map<CrsField, List<Integer>> fieldIndices = fieldIndices(lines.get(0));

            // Validate this looks like a CRS file using the headers offsets.
            final Optional<Map.Entry<CrsField, List<Integer>>> optMissingColumn = fieldIndices.entrySet().parallelStream()
                    .filter(entry -> entry.getValue().size() == 0).findFirst();
            if (optMissingColumn.isPresent()) {
                return new CrsParsedResult(Collections.emptyList(),
                        List.of(new CrsParseErrors("1",
                                List.of(String.format("Invalid CRS file format. Missing column %s",
                                        optMissingColumn.get().getKey())))));
            }

            final List<Tuple<Optional<CrsRecord>, Optional<CrsParseErrors>>> tuples = lines.stream().skip(1).map(row -> parseRow(row, fieldIndices)).collect(toList());
            return new CrsParsedResult(
                    tuples.stream().filter(tpl -> tpl.get_1().isPresent()).map(tpl -> tpl.get_1().get()).collect(toList()),
                    tuples.stream().filter(tpl1 -> tpl1.get_2().isPresent()).map(tpl1 -> tpl1.get_2().get()).collect(toList())
            );
        });

    }

    private Map<CrsField, List<Integer>> fieldIndices(String header) {
        return Catcher.convertToRuntime(() -> {
            final CSVRecord csvRecord = CSVParser.parse(header, CSVFormat.DEFAULT).getRecords().get(0);
            return Arrays.stream(CrsField.values()).map(field ->
                    tpl(field, indexOfField(csvRecord, field)))
                    .collect(toMap(tpl -> tpl.get_1(), tpl -> tpl.get_2()));
        });

    }

    private List<Integer> indexOfField(CSVRecord csvRecord, CrsField field) {
        return IntStream.range(0, csvRecord.size())
                .filter(index -> csvRecord.get(index).trim().equalsIgnoreCase(field.description)).mapToObj(index -> index).collect(toList());
    }


    private Tuple<Optional<CrsRecord>, Optional<CrsParseErrors>> parseRow(String row, Map<CrsField, List<Integer>> fieldIndices) {
        try {
            final CSVRecord csvRecord = CSVParser.parse(row, CSVFormat.DEFAULT).getRecords().get(0);

            CrsRecord crsRecord = CrsRecord.builder()
                    .id(null)
                    .gwfRef(fieldValue(fieldIndices, csvRecord, CrsField.GWF_REF, val -> val.get(0)))
                    .vafNo(fieldValue(fieldIndices, csvRecord, CrsField.VAF_NUMBER, val -> val.get(0)))
                    .casNo(fieldValue(fieldIndices, csvRecord, CrsField.CAS_NUMBER, val -> val.get(0)))
                    .cosNo(fieldValue(fieldIndices, csvRecord, CrsField.COS_NUMBER, val -> val.get(0)))
                    .postName(fieldValue(fieldIndices, csvRecord, CrsField.POST_NAME, val -> val.get((0))))
                    .familyName(fieldValue(fieldIndices, csvRecord, CrsField.FAMILY_NAME, val -> val.get(0)))
                    .otherName(fieldValue(fieldIndices, csvRecord, CrsField.OTHER_NAME, val -> val.get(0)))
                    .gender(fieldValue(fieldIndices, csvRecord, CrsField.GENDER, val -> Gender.parse(val.get(0))))
                    .dateOfBirth(fieldValue(fieldIndices, csvRecord, CrsField.DATE_OF_BIRTH, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .nationality(fieldValue(fieldIndices, csvRecord, CrsField.NATIONALITY, val -> val.get(0)))
                    .passportNumber(fieldValue(fieldIndices, csvRecord, CrsField.PASSPORT_NUMBER, val -> val.get(0)))
                    .mobileNumber(fieldValue(fieldIndices, csvRecord, CrsField.MOBILE_NUMBER, val -> val.get(0)))
                    .emailAddress(fieldValue(fieldIndices, csvRecord, CrsField.EMAIL_ADDRESS, val -> val.get(0)))
                    .localAddress(fieldValue(fieldIndices, csvRecord, CrsField.LOCAL_ADDRESS, val -> val.get(0)))
                    .status(fieldValue(fieldIndices, csvRecord, CrsField.STATUS_DETAILS, val -> VisaStatus.parse(val.get(0))))
                    .action(fieldValue(fieldIndices, csvRecord, CrsField.ACTION, val -> val.get(0)))
                    .reason(fieldValue(fieldIndices, csvRecord, CrsField.REASON, val -> val.get(0)))
                    .ecType(fieldValue(fieldIndices, csvRecord, CrsField.EC_TYPE, val -> val.get(0)))
                    .entryType(fieldValue(fieldIndices, csvRecord, CrsField.ENTRY_TYPE, val -> val.get(0)))
                    .visaEndorsement(fieldValue(fieldIndices, csvRecord, CrsField.VISA_ENDORSEMENT, val -> val.get(0)))
                    .validFrom(fieldValue(fieldIndices, csvRecord, CrsField.VISA_VALID_FROM, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .validTo(fieldValue(fieldIndices, csvRecord, CrsField.VISA_VALID_TO, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .sponsorName(fieldValue(fieldIndices, csvRecord, CrsField.SPONSOR_DETAILS_NAME, val -> val.get(0)))
                    .sponsorType(fieldValue(fieldIndices, csvRecord, CrsField.SPONSOR_DETAILS_TYPE, val -> val.get(0)))
                    .sponsorAddress(fieldValue(fieldIndices, csvRecord, CrsField.SPONSOR_DETAILS_ADDRESS, val -> val.get(0)))
                    .sponsorSpxNo(fieldValue(fieldIndices, csvRecord, CrsField.SPONSOR_DETAILS_SPX_NUMBER, val -> val.get(0)))
                    .additionalEndorsement1(fieldValue(fieldIndices, csvRecord, CrsField.ADDITIONAL_ENDORSEMENT_1, val -> val.get(0)))
                    .additionalEndorsement2(fieldValue(fieldIndices, csvRecord, CrsField.ADDITIONAL_ENDORSEMENT_2, val -> val.get(0)))
                    .catDEndors1(fieldValue(fieldIndices, csvRecord, CrsField.CAT_D_ENDORSEMENTS_1, val -> val.get(0)))
                    .catDEndors2(fieldValue(fieldIndices, csvRecord, CrsField.CAT_D_ENDORSEMENT_2, val -> val.get(0)))
                    .uniCollegeName(fieldValue(fieldIndices, csvRecord, CrsField.UNIVERSITY_COLLEGE_NAME, val -> val.get(0)))
                    .brpCollectionInfo(fieldValue(fieldIndices, csvRecord, CrsField.BRP_COLLECTION_INFORMATION, val -> val.get(0)))
                    .expectedTravelDate(fieldValue(fieldIndices, csvRecord, CrsField.EXPECTED_TRAVEL_DATE, val -> LocalDate.parse(val.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .build();


            return tpl(of(crsRecord), empty());
        } catch (Exception e) {
            return new Tuple<>(empty(), of(new CrsParseErrors(row, Collections.singletonList(e.getMessage()))));
        }
    }

    private <T> T fieldValue(Map<CrsField, List<Integer>> fieldIndices, CSVRecord csvRecord, CrsField field, Function<List<String>, T> mapper) {
        return fieldValue(fieldIndices, csvRecord, field, mapper, null);
    }

    private <T> T fieldValue(Map<CrsField, List<Integer>> fieldIndices, CSVRecord csvRecord, CrsField field, Function<List<String>, T> mapper, T defaultValue) {
        final List<Integer> indices = fieldIndices.get(field);
        if (indices.isEmpty()) return defaultValue;
        final List<String> collect = indices.stream().map(index -> valueForIndex(csvRecord, index)).filter(val -> val.isPresent()).map(val -> val.get()).collect(toList());
        return collect.isEmpty() ? defaultValue : mapper.apply(collect);
    }


    private Optional<String> valueForIndex(CSVRecord row, int index) {
        return ofNullable(row.get(index)).flatMap(val -> StringUtils.isNotBlank(val) ? of(val) : empty());
    }
}
