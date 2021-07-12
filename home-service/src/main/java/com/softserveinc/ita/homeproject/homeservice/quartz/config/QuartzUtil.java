package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;

@Slf4j
public class QuartzUtil {


    public static SimpleTrigger createSimpleTrigger(JobDetail jobDetail, int quartzTimeJob, String triggerName) {
        log.debug("createTrigger(jobDetail={}, pollFrequencyMs={}, triggerName={})",
                jobDetail.toString(), quartzTimeJob, triggerName);

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName,"AutoConfigured")
                .withSchedule(simpleSchedule()
                        .repeatForever()
                        .withIntervalInMilliseconds(quartzTimeJob)
                        .withMisfireHandlingInstructionNextWithRemainingCount())
                .build();
    }

    public static CronTrigger createCronTrigger(JobDetail jobDetail, String cron, String triggerName, String group) {
        log.debug("createTrigger(jobDetail={}, cron={}, triggerName={})",
                jobDetail.toString(), cron, triggerName);

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName,group)
                .withSchedule(cronSchedule(cron))
                .build();
    }


    public static JobDetail createJobDetail(Class<? extends Job> jobClass, String jobName,String group) {
        log.debug("createJobDe tail(jobClass={}, jobName={})", jobClass.getName(), jobName);

        return JobBuilder.newJob()
                .ofType(jobClass)
                .storeDurably(true)
                .withIdentity(jobName,group)
                .build();
    }
}


