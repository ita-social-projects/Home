package com.softserveinc.ita.homeproject.application.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class SendEmailJob extends BaseJobConfig implements Job {

    @Bean
    public JobDetailFactoryBean jobsDetails() {
        return JobConfig.createJobDetail(SendEmailJob.class, "Send Email job");
    }
    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail jobDetail) {
        return JobConfig.createTrigger(jobDetail, 6000, "Send Email trigger");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
