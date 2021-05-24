package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;


@Component
public class SendCooperationEmailJob extends SendEmailJob {

    private final CooperationInvitationService invitationService;

    public SendCooperationEmailJob(ServiceMapper mapper,
                                   MailService mailService,
                                   CooperationInvitationService invitationService) {
        super(mapper, mailService);
        this.invitationService = invitationService;
    }

    @SneakyThrows
    @Override
    protected void executeAllInvitationsByType() {
        List<CooperationInvitationDto> invitations = invitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            invitationService.updateSentDateTimeAndStatus(invite.getId(), LocalDateTime.now());
        }
    }

    @Override
    protected MailDto createMailDto(InvitationDto invitationDto) {
        CooperationInvitationDto invitation = mapper.convert(invitationDto, CooperationInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(invitation.getType());
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setRole(invitation.getRole().getName());
        //TODO: generate token link
        mailDto.setLink("Some link");
        mailDto.setCooperationName(invitation.getCooperationName());
        return mailDto;
    }
}
