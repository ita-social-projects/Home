package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public abstract class SendEmailJob extends QuartzJobBean {

    protected final ServiceMapper mapper;

    protected final MailService mailService;

    public SendEmailJob(ServiceMapper mapper, MailService mailService) {
        this.mapper = mapper;
        this.mailService = mailService;
    }

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        executeAllInvitationsByType();
    }

    protected abstract void executeAllInvitationsByType();

    protected abstract MailDto createMailDto(InvitationDto invitationDto);

    protected abstract String createLink();
}
