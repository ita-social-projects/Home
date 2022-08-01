package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.SendPasswordRestorationApprovalEmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
@QuartzJobBeanAutoConfiguration(cron = "${home.jobs.send-password-restoration-email.cron}")
public class SendPasswordRestorationApprovalEmailJob extends QuartzJobBean {

    private final SendPasswordRestorationApprovalEmailServiceImpl sendPasswordRestorationApprovalJob;

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) {
        sendPasswordRestorationApprovalJob.executeAllInvitationsByType();
    }
}
