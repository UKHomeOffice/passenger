package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import co.unruly.matchers.OptionalMatchers;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DailyWashFileCreatorTest {

    @Mock
    private CsvRowCreator csvRowCreator;

    @InjectMocks
    private DailyWashFileCreator dailyWashFileCreator;

    @Test
    public void whenRecordsArePresentFilesAreCreated() throws Exception {
        Instant now = Instant.parse("2017-12-03T10:15:30.00Z");

        VisaRecord visaRecord1 = new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("test"),
                Set.of(Tuple.tpl(new VisaRule(VisaRuleConstants.PASSPORT_NUMBER),
                        List.of(new VisaRuleContent(-1L, "PASSPORT_NUMBER", "passport-number-1", true, RuleType.USER_DATA)))));
        VisaRecord visaRecord2 = new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("test"),
                Set.of(Tuple.tpl(new VisaRule(VisaRuleConstants.PASSPORT_NUMBER),
                        List.of(new VisaRuleContent(-1L, "PASSPORT_NUMBER", "passport-number-2", true, RuleType.USER_DATA)))));

        List<VisaRecord> visaRecords = asList(visaRecord1, visaRecord2);

        when(csvRowCreator.docCheckRow(visaRecord1)).thenReturn("part1doc");
        when(csvRowCreator.nameCheckRow(visaRecord1)).thenReturn("part1name");
        when(csvRowCreator.docCheckRow(visaRecord2)).thenReturn("part2doc");
        when(csvRowCreator.nameCheckRow(visaRecord2)).thenReturn("part2name");

        Tuple3<Optional<File>, Optional<File>, Integer> result = dailyWashFileCreator.createFiles(visaRecords, now);

        assertThat(result._1.get().getName(), is("171203EVISADocWash.csv"));
        assertThat(readToString(result._1.get()), is("part1doc\npart2doc\n"));

        assertThat(result._2.get().getName(), is("171203EVISANameWash.csv"));
        assertThat(readToString(result._2.get()), is("part1name\npart2name\n"));

        assertThat(result._3, is(visaRecords.size()));
    }

    @Test
    public void whenRecordsAreNotPresentFilesAreNotCreated() throws Exception {
        Instant now = Instant.parse("2017-12-03T10:15:30.00Z");

        List<VisaRecord> visaRecords = emptyList();
        Tuple3<Optional<File>, Optional<File>, Integer> result = dailyWashFileCreator.createFiles(visaRecords, now);

        assertThat(result._1, is(OptionalMatchers.empty()));

        assertThat(result._2, is(OptionalMatchers.empty()));

        assertThat(result._3, is(0));

        verify(csvRowCreator, never()).docCheckRow(any(VisaRecord.class));
        verify(csvRowCreator, never()).nameCheckRow(any(VisaRecord.class));
    }

    private String readToString(File file) throws Exception {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new String(bytes, Charset.defaultCharset());
    }

}
