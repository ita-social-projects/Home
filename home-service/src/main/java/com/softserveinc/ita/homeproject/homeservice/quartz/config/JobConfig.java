package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import com.softserveinc.ita.homeproject.homeservice.quartz.jobs.SendApartmentEmailJob;
import com.softserveinc.ita.homeproject.homeservice.quartz.jobs.SendEmailJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;


@Slf4j
@Configuration
public class JobConfig implements BaseJobConfig {

    private static final int QUARTZ_TIME = 5000;

    @Override
    @Bean
    public JobDetailFactoryBean jobsDetails() {
        return JobConfig.createJobDetail(SendEmailJob.class, "Send Email job");
    }

    @Override
    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail jobDetail) {
        return JobConfig.createTrigger(jobDetail, QUARTZ_TIME, "Send Email trigger");
    }

    public static JobDetailFactoryBean createJobDetail(Class jobClass, String jobName) {
        log.debug("createJobDe tail(jobClass={}, jobName={})", jobClass.getName(), jobName);
        var factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail,
                                                         long pollFrequencyMs, String triggerName) {
        log.debug("createTrigger(jobDetail={}, pollFrequencyMs={}, triggerName={})",
            jobDetail.toString(), pollFrequencyMs, triggerName);
        var factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setName(triggerName);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }
}
