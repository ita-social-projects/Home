package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SendCooperationEmailJob extends BaseEmailJob {

    private final CooperationInvitationService cooperationInvitationService;

    @Override
    @SneakyThrows
    public void executeAllInvitationsByType() {
        List<CooperationInvitationDto> invitations = cooperationInvitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            cooperationInvitationService.updateSentDateTimeAndStatus(invite.getId());
        }
    }

    @Override
    protected MailDto createMailDto(InvitationDto invitationDto) {
        CooperationInvitationDto invitation = mapper.convert(invitationDto, CooperationInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.COOPERATION);
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setRole(invitation.getRole().getName());
        mailDto.setCooperationName(invitation.getCooperationName());
        checkRegistration(invitation, mailDto);
        return mailDto;
    }
}
