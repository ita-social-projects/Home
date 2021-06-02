package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

public interface BaseJobConfig {
    SimpleTriggerFactoryBean trigger(JobDetail jobDetail);

    JobDetailFactoryBean jobsDetails();
}
