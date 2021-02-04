package com.softserveinc.ita.homeproject.application.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class SimpleJobQuartz extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("ATestJob Running");
        try {
            System.out.println("Hello from Quartz");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
    }

