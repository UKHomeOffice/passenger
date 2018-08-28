package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.apache.commons.io.FileUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleMatcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CSVVisaRecordAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantsParserTest {

    @Mock
    private VisaRuleMatcher mockVisaRuleMatcher;

    @Mock
    private CSVVisaRecordAdapter csvVisaRecordAdapter;

    final String header = "Unique ID No,GWF Ref,VAF No,CAS No,First Name ,Middle Name," +
            "Surname,Gender,Date of Birth,Nationality,Passport No,Mobile Number ," +
            "Email address,Valid from,Valid Until,SPX number,Cat D Endorsement," +
            "Cat D Endorsement,Flight No,Departure Airport,Departure Date/Time ," +
            "Arrival Airport ,Arrival Date/Time," +
            "University/College name,ACL Address,action,reason";
    @InjectMocks
    ParticipantsParser participantsParser;

    @Test
    public void parseWellFormedRows() throws IOException {
        String row1 = "1,GWF046027237        ,1295789,E4G8DF0F36F0V1,TIM SPENCER,BARTHOLOMEW,GAMA,M,22/10/1996,USA,539701015,6316032284,test1@test.com,02/12/2017,31/12/2017,534MP6XH3," +
                "\"T4 G Student SPX Work limit 10 hrs,p/w term time.\",No Public Funds,BA1234,JFK,01/01/2018 09:00,LHR,01/01/2018 16:00,Ithaca College,\"74 Academia Row, Haytown, Surrey, AB0 1CD\",revoke,\"you lied!\"";
        String row2 = "2,GWF046063548        ,1295750,E4G0JJ1F30P0V4,KAREN ANNE,,ZETA,F,06/03/1997,USA,531939666,5089511730,test2@test.com,03/12/2017,31/12/2017,VF0319KH8," +
                "\"T4 G Student SPX Work limit 10 hrs,p/w term time.\",No Public Funds,BA1235,JFK,05/01/2018 15:30,LHR,05/01/2018 22:30,CAPA International Education,,,";

        File file = new File("test.csv");
        file.deleteOnExit();

        FileUtils.writeLines(file, asList(header, row1, row2));


        Visa visa1 = new VisaBuilder()
                .setPassportNumber("539701015")
                .setValidFrom(LocalDate.of(2017, 12, 2))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("534MP6XH3")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .setStatus(VisaStatus.REVOKED)
                .setReason("you lied!")
                .createVisa();

        Participant participant1 = new ParticipantBuilder()
                .setId(1L)
                .setGwf("GWF046027237")
                .setVaf("1295789")
                .setCas("E4G8DF0F36F0V1")
                .setFirstName("Tim Spencer")
                .setMiddleName("Bartholomew")
                .setSurName("Gama")
                .setDateOfBirth(LocalDate.of(1996, 10, 22))
                .setNationality("USA")
                .setPassportNumber("539701015")
                .setMobileNumber("6316032284")
                .setEmail("test1@test.com")
                .setInstitutionAddress("74 Academia Row, Haytown, Surrey, AB0 1CD")
                .createParticipant();

        Visa visa2 = new VisaBuilder()
                .setPassportNumber("531939666")
                .setValidFrom(LocalDate.of(2017, 12, 3))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("VF0319KH8")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .setStatus(VisaStatus.VALID)
                .createVisa();

        Participant participant2 = new ParticipantBuilder()
                .setId(2L)
                .setGwf("GWF046063548")
                .setVaf("1295750")
                .setCas("E4G0JJ1F30P0V4")
                .setFirstName("Karen Anne")
                .setSurName("Zeta")
                .setDateOfBirth(LocalDate.of(1997, 3, 6))
                .setNationality("USA")
                .setPassportNumber("531939666")
                .setMobileNumber("5089511730")
                .setEmail("test2@test.com")
                .createParticipant();

        VisaRecord visaRecord = new VisaRecord(VisaStatus.VALID, VisaType.createVisaType("test"), Collections.emptyList());
        when(csvVisaRecordAdapter.getVisaRecord(participant1, visa1)).thenReturn(visaRecord);
        when(csvVisaRecordAdapter.getVisaRecord(participant2, visa2)).thenReturn(visaRecord);

        when(mockVisaRuleMatcher.hasVisaRule(eq(visaRecord), any(Consumer.class))).thenReturn(true);

        assertThat(participantsParser.parse(file), equalTo(new ParsedResult(asList(tpl(participant1, visa1), tpl(participant2, visa2)), Collections.emptyList())));
    }

    @Test
    public void badlyFormedRows() throws IOException {
        String row1 = "1,GWF046027237        ,1295789,E4G8DF0F36F0V1,TIM SPENCER,BARTHOLOMEW,GAMA,M,10 Oct 1996,USA,539701015,6316032284,test1@test.com,02/12/2017,31/12/2017,534MP6XH3," +
                "\"T4 G Student SPX Work limit 10 hrs,p/w term time.\",No Public Funds,BA1234,JFK,\"Jan 1, 2018, 1:30 PM\", LHR,01/01/2018 16:00,Ithaca College,\"74 Academia Row, Haytown, Surrey, AB0 1CD\",,";
        String row2 = "2,GWF046063548        ,1295750,E4G0JJ1F30P0V4,KAREN ANNE,,ZETA,F,06/03/1997,USA,531939666,5089511730,test2@test.com,03/12/2017,31/12/2017,VF0319KH8," +
                "\"T4 G Student SPX Work limit 10 hrs,p/w term time.\",No Public Funds,BA1235,JFK,05/01/2018 15:30,LHR,05/01/2018 22:30,CAPA International Education ,,,";

        File file = new File("test.csv");
        file.deleteOnExit();

        FileUtils.writeLines(file, asList(header, row1, row2));

        Visa visa = new VisaBuilder()
                .setPassportNumber("531939666")
                .setValidFrom(LocalDate.of(2017, 12, 3))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("VF0319KH8")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .setStatus(VisaStatus.VALID)
                .createVisa();

        Participant participant = new ParticipantBuilder()
                .setId(2L)
                .setGwf("GWF046063548")
                .setVaf("1295750")
                .setCas("E4G0JJ1F30P0V4")
                .setFirstName("Karen Anne")
                .setSurName("Zeta")
                .setDateOfBirth(LocalDate.of(1997, 3, 6))
                .setNationality("USA")
                .setPassportNumber("531939666")
                .setMobileNumber("5089511730")
                .setEmail("test2@test.com")
                .createParticipant();

        VisaRecord visaRecord = new VisaRecord(VisaStatus.VALID, VisaType.createVisaType("test"), Collections.emptyList());
        when(csvVisaRecordAdapter.getVisaRecord(participant, visa)).thenReturn(visaRecord);

        when(mockVisaRuleMatcher.hasVisaRule(eq(visaRecord), any(Consumer.class))).thenReturn(true);

        assertThat(participantsParser.parse(file).participants, equalTo(singletonList(tpl(participant, visa))));
        assertThat(participantsParser.parse(file).parseErrors.size(), equalTo(1));
        assertThat(participantsParser.parse(file).parseErrors.get(0).csvRow, equalTo(row1));
        assertThat(participantsParser.parse(file).parseErrors.get(0).exception, instanceOf(DateTimeParseException.class));
    }

    @Test
    public void testForNonNullFields() throws IOException {
        String row1 = "1,GWF046027237        ,1295789,E4G8DF0F36F0V1,TIM SPENCER,BARTHOLOMEW,GAMA,M,22/10/1996,USA,,6316032284,test1@test.com,02/12/2017,31/12/2017,534MP6XH3," +
                "\"T4 G Student SPX Work limit 10 hrs,p/w term time.\",No Public Funds,BA1234,JFK,01/01/2018 09:00,LHR,01/01/2018 16:00,Ithaca College,\"74 Academia Row, Haytown, Surrey, AB0 1CD\",,";
        String row2 = "2,GWF046063548        ,1295750,E4G0JJ1F30P0V4,KAREN ANNE,,ZETA,F,06/03/1997,USA,531939666,5089511730,test2@test.com,03/12/2017,31/12/2017,VF0319KH8," +
                "\"T4 G Student SPX Work limit 10 hrs,p/w term time.\",No Public Funds,BA1235,JFK,05/01/2018 15:30,LHR,05/01/2018 22:30,CAPA International Education ,,,";

        File file = new File("test.csv");
        file.deleteOnExit();

        FileUtils.writeLines(file, asList(header, row1, row2));

        Visa visa = new VisaBuilder()
                .setPassportNumber("531939666")
                .setValidFrom(LocalDate.of(2017, 12, 3))
                .setValidTo(LocalDate.of(2017, 12, 31))
                .setSpx("VF0319KH8")
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .setStatus(VisaStatus.VALID)
                .createVisa();

        Participant participant = new ParticipantBuilder()
                .setId(2l)
                .setGwf("GWF046063548")
                .setVaf("1295750")
                .setCas("E4G0JJ1F30P0V4")
                .setFirstName("Karen Anne")
                .setSurName("Zeta")
                .setDateOfBirth(LocalDate.of(1997, 3, 6))
                .setNationality("USA")
                .setPassportNumber("531939666")
                .setMobileNumber("5089511730")
                .setEmail("test2@test.com")
                .createParticipant();

        VisaRecord visaRecord = new VisaRecord(VisaStatus.VALID, VisaType.createVisaType("test"), Collections.emptyList());
        when(csvVisaRecordAdapter.getVisaRecord(participant, visa)).thenReturn(visaRecord);

        when(mockVisaRuleMatcher.hasVisaRule(eq(visaRecord), any(Consumer.class))).thenReturn(true);

        final ParsedResult parsedResult = participantsParser.parse(file);
        assertThat(parsedResult.participants, equalTo(singletonList(tpl(participant, visa))));
        assertThat(parsedResult.parseErrors.size(), equalTo(1));
        assertThat(parsedResult.parseErrors.get(0).csvRow, equalTo(row1));
        assertThat(parsedResult.parseErrors.get(0).exception, instanceOf(NullPointerException.class));

    }

}
