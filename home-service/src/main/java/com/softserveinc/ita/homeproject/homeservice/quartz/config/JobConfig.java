package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import com.softserveinc.ita.homeproject.homeservice.quartz.jobs.SendEmailJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Slf4j
@Configuration
public class JobConfig implements BaseJobConfig {
    private static int quartzTime;

    private static final String TRIGGER_NAME = "Send email trigger";

    @Override
    @Bean
    public JobDetailFactoryBean jobsDetails() {
        return JobConfig.createJobDetail(SendEmailJob.class, "Send Email job");
    }

    @Override
    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail jobDetail) {
        return JobConfig.createTrigger(jobDetail);
    }

    public static JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClass, String jobName) {
        log.debug("createJobDe tail(jobClass={}, jobName={})", jobClass.getName(), jobName);
        var factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail) {
        log.debug("createTrigger(jobDetail={}, pollFrequencyMs={}, triggerName={})",
            jobDetail.toString(), quartzTime, TRIGGER_NAME);
        var factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(quartzTime);
        factoryBean.setName(TRIGGER_NAME);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

    @Value("${quartz.time.job}")
    public void setQuartzTime(int quartzTime) {
        JobConfig.quartzTime = quartzTime;
    }
}
