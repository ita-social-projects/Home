package com.softserveinc.ita.homeproject.homeservice.service.email;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.invitation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.invitation.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.invitation.CooperationInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendCooperationEmailService extends BaseEmailService {

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
    public MailDto createMailDto(InvitationDto invitationDto) {
        CooperationInvitationDto invitation = mapper.convert(invitationDto, CooperationInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.COOPERATION);
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setRegistrationToken(invitation.getRegistrationToken());
        mailDto.setRole(invitation.getRole().getName());
        mailDto.setCooperationName(invitation.getCooperationName());
        checkRegistration(invitation, mailDto);
        return mailDto;
    }
}
