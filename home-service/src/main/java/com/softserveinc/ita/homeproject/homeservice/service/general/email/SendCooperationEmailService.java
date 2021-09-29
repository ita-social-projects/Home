package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation.CooperationInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendCooperationEmailService extends BaseEmailService {

    private static final String NOT_FOUND_COOPERATION_FORMAT = "Can't find cooperation with given ID: %d";

    private final CooperationInvitationService cooperationInvitationService;

    private final CooperationRepository cooperationRepository;

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

        Cooperation coop = cooperationRepository.findById(invitation.getCooperationId())
                .filter(Cooperation::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(
                        String.format(NOT_FOUND_COOPERATION_FORMAT, invitation.getCooperationId())));

        var mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.COOPERATION);
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setRegistrationToken(invitation.getRegistrationToken());
        mailDto.setRole(invitation.getRole().getName());
        mailDto.setCooperationAdminEmail(coop.getAdminEmail());
        mailDto.setCooperationName(coop.getName());
        checkRegistration(invitation, mailDto);
        return mailDto;
    }
}
