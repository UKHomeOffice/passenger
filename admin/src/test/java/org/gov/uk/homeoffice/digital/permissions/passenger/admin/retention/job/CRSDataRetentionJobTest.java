package org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.job;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.service.RetentionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CRSDataRetentionJobTest {

    @Mock
    private RetentionService mockRetentionService;

    @InjectMocks
    private CRSDataRetentionJob underTest;

    @Test
    public void shouldExecuteJob() throws Exception {
        final JobExecutionContext context = mock(JobExecutionContext.class);
        final JobDetail mockJobDetail = mock(JobDetail.class);
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAsString("scheduleDurationDays", 10);

        when(context.getJobDetail()).thenReturn(mockJobDetail);
        when(mockJobDetail.getJobDataMap()).thenReturn(jobDataMap);

        underTest.executeInternal(context);

        verify(mockRetentionService).removeOlderThan(any(LocalDateTime.class));
    }

}
