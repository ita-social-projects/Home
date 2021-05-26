package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.service.impl.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class SendEmailJob extends QuartzJobBean {

    private final SendApartmentEmailJob sendApartmentEmailJob;

    private final SendCooperationEmailJob sendCooperationEmailJob;

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) {
        sendApartmentEmailJob.executeAllInvitationsByType();
        sendCooperationEmailJob.executeAllInvitationsByType();
    }
}
