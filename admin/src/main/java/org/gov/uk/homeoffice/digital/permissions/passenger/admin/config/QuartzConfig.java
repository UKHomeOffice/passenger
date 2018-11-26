package org.gov.uk.homeoffice.digital.permissions.passenger.admin.config;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.job.CRSDataRetentionJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${job.retention.schedule.days}")
    private Integer scheduleDurationDays;

    @Value("${job.retention.execution.minutes}")
    private Integer executionInterval;

    @Bean
    public JobDetail crsRetentionJobDetail() {
        return JobBuilder.newJob(CRSDataRetentionJob.class)
                .withIdentity("crsDataRetentionJob")
                .withDescription("CRS data retention job")
                .usingJobData("scheduleDurationDays", scheduleDurationDays)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sampleJobTrigger() {
        final SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(executionInterval).repeatForever();
        return TriggerBuilder.newTrigger().forJob(crsRetentionJobDetail())
                .withIdentity("crsRetentionTrigger").withSchedule(scheduleBuilder).build();
    }

}
