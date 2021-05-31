package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SendEmailJob extends QuartzJobBean {

    private final SendCooperationEmailJob sendCooperationEmailJob;

    public SendEmailJob(SendCooperationEmailJob sendCooperationEmailJob) {
        this.sendCooperationEmailJob = sendCooperationEmailJob;
    }

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        sendCooperationEmailJob.executeAllInvitationsByType();
    }

}
