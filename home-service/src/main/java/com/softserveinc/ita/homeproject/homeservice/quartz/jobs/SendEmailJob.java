package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmailJob extends QuartzJobBean {
    private final MailService mailService;

    private final InvitationService invitationService;

    public SendEmailJob(MailService mailService, InvitationService invitationService) {
        this.mailService = mailService;
        this.invitationService = invitationService;
    }

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<InvitationDto> invitations = invitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            log.info("send email by invitation id: {}", invite.getId());
            mailService.sendTextMessage(createMailDto(invite));
            invitationService.updateSentDateTime(invite.getId(), LocalDateTime.now());
        }
    }

    private MailDto createMailDto(InvitationDto invitationDto) {
        MailDto mailDto = new MailDto();
        mailDto.setId(invitationDto.getId());
        mailDto.setEmail(invitationDto.getEmail());
        mailDto.setName(invitationDto.getName());
        mailDto.setRoleName(invitationDto.getRole().getName());
        //TODO: generate token link
        mailDto.setLink("");
        //TODO: finish when cooperation implementation will be done
        mailDto.setCooperationName("cooperation");
        return mailDto;
    }
}
