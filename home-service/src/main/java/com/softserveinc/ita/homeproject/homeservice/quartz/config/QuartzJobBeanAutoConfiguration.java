package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.quartz.Trigger;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * QuartzJobBeanAutoConfiguration annotation for @see org.quartz.Job
 * For Job annotated with QuartzJobBeanAutoConfiguration will be generated
 * {@link Trigger} and {@link org.quartz.JobDetail} beans
 * Trigger Example:
 *         return TriggerBuilder.newTrigger()
 *                 .forJob(jobDetail)
 *                 .withIdentity(triggerName)
 *                 .withSchedule(simpleSchedule()
 *                         .repeatForever()
 *                         .withIntervalInMilliseconds({@link QuartzJobBeanAutoConfiguration#cron()})
 *                         .withMisfireHandlingInstructionNextWithRemainingCount())
 *                 .build();
 *
 * JobDetail Example:
 *         return JobBuilder.newJob()
 *                         .ofType(jobClass)
 *                         .storeDurably(true)
 *                         .withIdentity(jobName)
 *                         .build();
 */
public @interface QuartzJobBeanAutoConfiguration {
    String cron();
    String group() default "AutoConfigured";
    String jobName() default "";
    String triggerName() default "";
}
