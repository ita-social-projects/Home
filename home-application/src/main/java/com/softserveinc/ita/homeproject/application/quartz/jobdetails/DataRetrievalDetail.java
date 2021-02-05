package com.softserveinc.ita.homeproject.application.quartz.jobdetails;

import com.softserveinc.ita.homeproject.application.quartz.config.QuartzConfig;
import com.softserveinc.ita.homeproject.application.quartz.jobs.DataRetrievalJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;


@Configuration
public class DataRetrievalDetail {
    @Bean(name = "dataRetrieval")
    public JobDetailFactoryBean jobMemberStats() {
        return QuartzConfig.createJobDetail(DataRetrievalJob.class, "Data retrieval job");
    }
}
