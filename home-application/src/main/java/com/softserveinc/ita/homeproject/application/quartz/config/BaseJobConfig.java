package com.softserveinc.ita.homeproject.application.quartz.config;

import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

public abstract class BaseJobConfig {
    public abstract SimpleTriggerFactoryBean trigger(JobDetail jobDetail);
    public abstract JobDetailFactoryBean jobsDetails();
}
