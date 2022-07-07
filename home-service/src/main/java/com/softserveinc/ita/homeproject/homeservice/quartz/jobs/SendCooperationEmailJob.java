package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.SendCooperationEmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@QuartzJobBeanAutoConfiguration(cron = "${home.jobs.send-cooperation-email.cron}")
public class SendCooperationEmailJob extends QuartzJobBean {

    private final SendCooperationEmailService sendCooperationEmailJob;

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) {
        sendCooperationEmailJob.executeAllInvitationsByType();
    }
}
