package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.SendApartmentEmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@QuartzJobBeanAutoConfiguration(cron = "${home.jobs.send-apartment-email.cron}")
public class SendApartmentEmailJob extends QuartzJobBean {

    private final SendApartmentEmailService sendApartmentEmailJob;

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) {
        sendApartmentEmailJob.executeAllInvitationsByType();
    }
}
