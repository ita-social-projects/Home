package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.impl.SendApartmentEmailService;
import com.softserveinc.ita.homeproject.homeservice.service.impl.SendCooperationEmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
@QuartzJobBeanAutoConfiguration(cron = "0/5 0/1 * 1/1 * ? *")
public class SendEmailJob extends QuartzJobBean {

    private final SendApartmentEmailService sendApartmentEmailJob;

    private final SendCooperationEmailService sendCooperationEmailJob;

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) {
        log.info("Send email JOBA");
        sendApartmentEmailJob.executeAllInvitationsByType();
        sendCooperationEmailJob.executeAllInvitationsByType();
    }
}
