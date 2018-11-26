package org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.job;

import lombok.NoArgsConstructor;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.service.RetentionService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@NoArgsConstructor
public class CRSDataRetentionJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRSDataRetentionJob.class);

    @Autowired
    private RetentionService retentionService;

    @Override
    protected void executeInternal(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.debug("Executing CRS data retention job...");
        final Integer days = jobExecutionContext.getJobDetail().getJobDataMap()
                .getInt("scheduleDurationDays");
        final LocalDateTime from = LocalDateTime.now().minus(days, ChronoUnit.DAYS);
        retentionService.removeOlderThan(from);
    }

}
