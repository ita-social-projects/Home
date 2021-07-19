package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * QuartzJobBeanAutoConfiguration annotation for {@link Job}
 * For Job annotated with QuartzJobBeanAutoConfiguration will be generated
 * {@link Trigger} and {@link JobDetail} beans
 * Trigger Example:
 *         return TriggerBuilder.newTrigger()
 *                 .forJob(jobDetail)
 *                 .withIdentity({@link QuartzJobBeanAutoConfiguration#triggerName()},
 *                               {@link QuartzJobBeanAutoConfiguration#group()})
 *                 .withSchedule(cronSchedule({@link QuartzJobBeanAutoConfiguration#cron()}))
 *                 .build();
 *
 * JobDetail Example:
 *         return JobBuilder.newJob()
 *                         .ofType(jobClass)
 *                         .storeDurably(true)
 *                         .withIdentity({@link QuartzJobBeanAutoConfiguration#jobName()},
 *                                       {@link QuartzJobBeanAutoConfiguration#group()})
 *                         .build();
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzJobBeanAutoConfiguration {
    String cron();
    String group() default "AutoConfigured";
    String jobName() default "";
    String triggerName() default "";
}
