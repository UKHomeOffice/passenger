package org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CRSRetentionServiceBeanTest {

    @Mock
    private CrsRecordRepository mockCrsRecordRepository;

    @InjectMocks
    private CRSRetentionServiceBean underTest;

    @Test
    public void shouldExecuteRetention() {
        LocalDateTime now = LocalDateTime.now();
        underTest.removeOlderThan(now);
        verify(mockCrsRecordRepository).deleteOlderThan(now);
    }

}
