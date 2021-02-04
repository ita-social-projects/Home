package com.softserveinc.ita.homeproject.application.quartz.job;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class RunJobClass {

    @Bean(name = "simpleJobQuartz")
    public JobDetailFactoryBean jobMemberStats() {
        return QuartzConfig.createJobDetail(SimpleJobQuartz.class, "Simple Job");
    }

    @Bean(name = "simpleJobTrigger")
    public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("simpleJobQuartz") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, 6000, "Simple Job Trigger");
    }
}
