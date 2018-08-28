package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.UnknownAdapterException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantsRecordRepositoryBeanTest {

    ParticipantRecordRepositoryBean testObject;

    @Mock
    CSVParticipantRepository csvParticipantRepository;

    @Mock
    CRSParticipantRepository crsParticipantRepository;

    @Before
    public void setUp() throws Exception {
        when(crsParticipantRepository.getType()).thenReturn("CRS");
        when(csvParticipantRepository.getType()).thenReturn("CSV");
    }

    @Test
    public void delegateToCRSAdapterWhenTheDataSourceIsCrs() {

        testObject = new ParticipantRecordRepositoryBean("CRS", Arrays.asList(crsParticipantRepository, csvParticipantRepository));

        testObject.getAllParticipantsWithVisas();

        verify(crsParticipantRepository, times(1)).getAllParticipantsWithVisas();

    }

    @Test
    public void getAdapterWhenTheDataSourceIsCsv() {
        testObject = new ParticipantRecordRepositoryBean("CSV", Arrays.asList(crsParticipantRepository, csvParticipantRepository));

        testObject.getAllParticipantsWithVisas();

        verify(csvParticipantRepository, times(1)).getAllParticipantsWithVisas();

    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Test
    public void getAdapterWhenTheDataSourceIsNotFound() {

        exception.expect(UnknownAdapterException.class);

        testObject = new ParticipantRecordRepositoryBean("TEST", Arrays.asList(crsParticipantRepository, csvParticipantRepository));

        testObject.getAllParticipantsWithVisas();

    }
}
