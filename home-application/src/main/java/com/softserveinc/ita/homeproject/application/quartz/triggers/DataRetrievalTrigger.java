package com.softserveinc.ita.homeproject.application.quartz.triggers;

import com.softserveinc.ita.homeproject.application.quartz.config.QuartzConfig;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class DataRetrievalTrigger {
    @Bean(name = "dataRetrievalTriggers")
    public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("dataRetrieval") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, 60000, "Data retrieval job trigger");
    }
}
