package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;

@Slf4j
public final class QuartzUtil {

    private QuartzUtil() {
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


