package com.softserveinc.ita.homeproject.homeservice.quartz.config;

import static com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzUtil.createCronTrigger;
import static com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzUtil.createJobDetail;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class QuartzBeanPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
            throws BeansException {
        String[] names =  configurableListableBeanFactory.getBeanNamesForType(Job.class);

        for(String name : names) {
            Class<? extends Job> clazz = (Class<? extends Job>) configurableListableBeanFactory.getType(name);
            if (clazz != null && clazz.isAnnotationPresent(QuartzJobBeanAutoConfiguration.class)) {
                QuartzJobBeanAutoConfiguration quartzConfig =
                        clazz.getDeclaredAnnotation(QuartzJobBeanAutoConfiguration.class);

                String jobName = quartzConfig.jobName().isEmpty()
                        ? name + "JobDetail" : quartzConfig.jobName();
                String triggerName = quartzConfig.triggerName().isEmpty()
                        ? name + "Trigger" : quartzConfig.triggerName();

                JobDetail jobDetail = createJobDetail(clazz, jobName,quartzConfig.group());
                String cronExpression = environment.resolvePlaceholders(quartzConfig.cron());
                Trigger trigger = createCronTrigger(jobDetail, cronExpression, triggerName, quartzConfig.group());

                configurableListableBeanFactory.registerSingleton(name + "JobDetail", jobDetail);
                configurableListableBeanFactory.registerSingleton(name + "Trigger", trigger);
            }
        }
    }
}
