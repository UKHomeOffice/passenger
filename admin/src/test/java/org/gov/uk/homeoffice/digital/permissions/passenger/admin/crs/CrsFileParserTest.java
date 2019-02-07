package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.apache.commons.io.FileUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service.CountryService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Gender;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrsFileParserTest {

    private final String header = "GWF Ref," +
            "VAF No," +
            "CAS No," +
            "COS No," +
            "Post name," +
            "Family name," +
            "Other names," +
            "Gender," +
            "Date of Birth," +
            "Nationality," +
            "Passport No," +
            "Mobile No," +
            "Email address," +
            "Local Address, " +
            "Status details," +
            "EC Type," +
            "Entry Type," +
            "Visa endorsement," +
            "Visa valid from," +
            "Visa valid to," +
            "Tier 5 - Work until date," +
            "Sponsor details: Name," +
            "Sponsor details: Type," +
            "Sponsor details: Address," +
            "Sponsor details: SPX No," +
            "Additional endorsement 1," +
            "Additional endorsement 2," +
            "Cat D endorsement 1," +
            "Cat D endorsement 2," +
            "University/College name," +
            "BRP Collection information," +
            "Expected travel date," +
            "action," +
            "reason";

    @Mock
    private CountryService mockCountryService;

    @InjectMocks
    private CrsFileParser testObject;

    private static List<Country> countries = newArrayList(
            new Country(Locale.CHINA, true, "CHN", LocalDateTime.now(), LocalDateTime.now())
    );

    @Test
    public void parseWellFormedRows() throws IOException {
        String row1 = "GWF046284828,143757,E4G9XI0F39W0VX,SOMECOSNUMBER,MADR,SMITH,JOHN,M,08/11/1996,CHN,234567891,123-456-7890,JOHN.SMITH@DUMMY.AC.UK,1 STREET LANE,ISSUED,D,MULT,TIER 4 (GENERAL) STUDENT,06/06/2018,05/07/2018,,Lancaster University,UNIVERSITY,Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM,RGRCCJNF8,Additional 1,Additional 2,T4 G Student SPX* Work limit 20 hrs,p/w term time. No Public Funds.Police Registration,Lancaster University,Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM,,,";
        String row2 = "GWF046284829,143756,E4G9XI0F41W0VX,,BGC,APPLE,BARRY,M,22/10/1996,USA,234567892,123-456-7891,BARRY.APPLE@DUMMY.AC.UK,2 STREET LANE,ISSUED,D,SINGLE,TIER 4 (GENERAL) STUDENT,01/01/2018,30/06/2018,30/06/2018,Manchester University ,UNIVERSITY,Manc Uni MA1 4YW UNITED KINGDOM,RGRCCJNF9,Additional 3,Additional 4,T4 G Student SPX*,p/w term time. No Public Funds.Police Registration,Manchester University ,Manchester Univeristy MA1 4YW UNITED KINGDOM,,,";

        File file = new File("test.csv");
        file.deleteOnExit();

        FileUtils.writeLines(file, asList(header, row1, row2));

        CrsRecord crsRecord = CrsRecord.builder()
                .id(null)
                .gwfRef("GWF046284828")
                .vafNo("143757")
                .casNo("E4G9XI0F39W0VX")
                .cosNo("SOMECOSNUMBER")
                .postName("MADR")
                .familyName("SMITH")
                .otherName("JOHN")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.parse("08/11/1996", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .nationality("CHN")
                .passportNumber("234567891")
                .mobileNumber("123-456-7890")
                .emailAddress("JOHN.SMITH@DUMMY.AC.UK")
                .localAddress("1 STREET LANE")
                .status(VisaStatus.ISSUED)
                .ecType("D")
                .entryType("MULT")
                .visaEndorsement("TIER 4 (GENERAL) STUDENT")
                .validFrom(LocalDate.parse("06/06/2018", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .validTo((LocalDate.parse("05/07/2018", DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .sponsorName("Lancaster University")
                .sponsorType("UNIVERSITY")
                .sponsorAddress("Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM")
                .sponsorSpxNo("RGRCCJNF8")
                .additionalEndorsement1("Additional 1")
                .additionalEndorsement2("Additional 2")
                .catDEndors1("T4 G Student SPX* Work limit 20 hrs")
                .catDEndors2("p/w term time. No Public Funds.Police Registration")
                .uniCollegeName("Lancaster University")
                .brpCollectionInfo("Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM")
                .expectedTravelDate(null)
                .build();

        when(mockCountryService.getCountries()).thenReturn(countries);

        CrsParsedResult parsedResult = testObject.parse(file);

        assertCrsRecords(crsRecord, parsedResult);
    }

    @Test
    public void parseRowsWithErrors() throws IOException {
        String row1 = "GWF046284828,143757,E4G9XI0F39W0VX,SOMECOSNUMBER,MADR,SMITH,JOHN,M,08/11/1996,CHN,234567891,123-456-7890,JOHN.SMITH@DUMMY.AC.UK,1 STREET LANE,ISSUED,D,MULT,TIER 4 (GENERAL) STUDENT,06/06/2018,05/07/2018,,Lancaster University,UNIVERSITY,Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM,RGRCCJNF8,Additional 1,Additional 2,T4 G Student SPX* Work limit 20 hrs,p/w term time. No Public Funds.Police Registration,Lancaster University,Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM,,,";
        String badRowWithWrongDateFormat = "GWF046284828,143757,E4G9XI0F39W0VX,SOMECOSNUMBER,MADR,M,08/11/ds,CHN,234567891,123-456-7890,JOHN.SMITH@DUMMY.AC.UK,1 STREET LANE,ISSUED,D,MULT,TIER 4 (GENERAL) STUDENT,06/06/2018,05/07/2018,,Lancaster University,UNIVERSITY,Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM,RGRCCJNF8,,,T4 G Student SPX* Work limit 20 hrs,p/w term time. No Public Funds.Police Registration,Lancaster University,Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM,,,";

        File file = new File("test.csv");
        file.deleteOnExit();

        FileUtils.writeLines(file, asList(header, row1, badRowWithWrongDateFormat));

        CrsRecord crsRecord = CrsRecord.builder()
                .id(null)
                .gwfRef("GWF046284828")
                .vafNo("143757")
                .casNo("E4G9XI0F39W0VX")
                .cosNo("SOMECOSNUMBER")
                .postName("MADR")
                .familyName("SMITH")
                .otherName("JOHN")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.parse("08/11/1996", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .nationality("CHN")
                .passportNumber("234567891")
                .mobileNumber("123-456-7890")
                .emailAddress("JOHN.SMITH@DUMMY.AC.UK")
                .localAddress("1 STREET LANE")
                .status(VisaStatus.ISSUED)
                .ecType("D")
                .entryType("MULT")
                .visaEndorsement("TIER 4 (GENERAL) STUDENT")
                .validFrom(LocalDate.parse("06/06/2018", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .validTo((LocalDate.parse("05/07/2018", DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .sponsorName("Lancaster University")
                .sponsorType("UNIVERSITY")
                .sponsorAddress("Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM")
                .sponsorSpxNo("RGRCCJNF8")
                .additionalEndorsement1("Additional 1")
                .additionalEndorsement2("Additional 2")
                .catDEndors1("T4 G Student SPX* Work limit 20 hrs")
                .catDEndors2("p/w term time. No Public Funds.Police Registration")
                .uniCollegeName("Lancaster University")
                .brpCollectionInfo("Bailrigg LANCASTER LANCASHIRE LA1 4YW UNITED KINGDOM")
                .expectedTravelDate(null)
                .build();

        when(mockCountryService.getCountries()).thenReturn(countries);

        CrsParsedResult parsedResult = testObject.parse(file);

        assertCrsRecords(crsRecord, parsedResult);

        assertThat(parsedResult.getParseErrors().get(0).crsRow, equalTo("Unable to parse "));
        assertThat(parsedResult.getParseErrors().get(0).message.get(0), equalTo("Text '234567891' could not be parsed at index 2"));
    }

    private void assertCrsRecords(CrsRecord crsRecord, CrsParsedResult parsedResult) {
        CrsRecord crsRecord1 = parsedResult.getCrsRecords().get(0);

        assertThat(crsRecord1.getGwfRef(), equalTo(crsRecord.getGwfRef()));
        assertThat(crsRecord1.getVafNo(), equalTo(crsRecord.getVafNo()));
        assertThat(crsRecord1.getCasNo(), equalTo(crsRecord.getCasNo()));
        assertThat(crsRecord1.getCosNo(), equalTo(crsRecord.getCosNo()));
        assertThat(crsRecord1.getPostName(), equalTo(crsRecord.getPostName()));
        assertThat(crsRecord1.getFamilyName(), equalTo(crsRecord.getFamilyName()));
        assertThat(crsRecord1.getOtherName(), equalTo(crsRecord.getOtherName()));
        assertThat(crsRecord1.getGender(), equalTo(crsRecord.getGender()));
        assertThat(crsRecord1.getDateOfBirth(), equalTo(crsRecord.getDateOfBirth()));
        assertThat(crsRecord1.getNationality(), equalTo(crsRecord.getNationality()));
        assertThat(crsRecord1.getPassportNumber(), equalTo(crsRecord.getPassportNumber()));
        assertThat(crsRecord1.getMobileNumber(), equalTo(crsRecord.getMobileNumber()));
        assertThat(crsRecord1.getEmailAddress(), equalTo(crsRecord.getEmailAddress()));
        assertThat(crsRecord1.getLocalAddress(), equalTo(crsRecord1.getLocalAddress()));
        assertThat(crsRecord1.getStatus(), equalTo(crsRecord.getStatus()));
        assertThat(crsRecord1.getAction(), equalTo(crsRecord.getAction()));
        assertThat(crsRecord1.getReason(), equalTo(crsRecord.getReason()));
        assertThat(crsRecord1.getEcType(), equalTo(crsRecord.getEcType()));
        assertThat(crsRecord1.getEntryType(), equalTo(crsRecord.getEntryType()));
        assertThat(crsRecord1.getVisaEndorsement(), equalTo(crsRecord.getVisaEndorsement()));
        assertThat(crsRecord1.getValidFrom(), equalTo(crsRecord.getValidFrom()));
        assertThat(crsRecord1.getValidTo(), equalTo(crsRecord.getValidTo()));
        assertThat(crsRecord1.getSponsorName(), equalTo(crsRecord.getSponsorName()));
        assertThat(crsRecord1.getSponsorType(), equalTo(crsRecord.getSponsorType()));
        assertThat(crsRecord1.getSponsorAddress(), equalTo(crsRecord.getSponsorAddress()));
        assertThat(crsRecord1.getSponsorSpxNo(), equalTo(crsRecord.getSponsorSpxNo()));
        assertThat(crsRecord1.getAdditionalEndorsement1(), equalTo(crsRecord.getAdditionalEndorsement1()));
        assertThat(crsRecord1.getAdditionalEndorsement2(), equalTo(crsRecord.getAdditionalEndorsement2()));
        assertThat(crsRecord1.getCatDEndors1(), equalTo(crsRecord.getCatDEndors1()));
        assertThat(crsRecord1.getCatDEndors2(), equalTo(crsRecord.getCatDEndors2()));
        assertThat(crsRecord1.getUniCollegeName(), equalTo(crsRecord.getUniCollegeName()));
        assertThat(crsRecord1.getBrpCollectionInfo(), equalTo(crsRecord.getBrpCollectionInfo()));
        assertThat(crsRecord1.getExpectedTravelDate(), equalTo(crsRecord.getExpectedTravelDate()));
    }

}
