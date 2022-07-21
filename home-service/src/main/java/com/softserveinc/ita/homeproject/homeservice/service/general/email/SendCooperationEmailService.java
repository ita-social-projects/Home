package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_COOPERATION_FORMAT_MESSAGE;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SendCooperationEmailService extends BaseEmailService {

    private final InvitationService<CooperationInvitation, CooperationInvitationDto> invitationService;

    public SendCooperationEmailService(
        @Qualifier(value = "cooperationInvitationServiceImpl")
            InvitationService<CooperationInvitation, CooperationInvitationDto> invitationService,
        CooperationRepository cooperationRepository) {
        this.invitationService = invitationService;
        this.cooperationRepository = cooperationRepository;
    }

    @Override
    protected MailDto createBaseMailDto(Mailable letter) {
        CooperationInvitationDto invitation = mapper.convert(letter, CooperationInvitationDto.class);
        Cooperation coop = cooperationRepository.findById(invitation.getCooperationId())
            .filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(
                String.format(NOT_FOUND_COOPERATION_FORMAT_MESSAGE, invitation.getCooperationId())));
        MailDto mailDto = new MailDto();
        mailDto.setRole(invitation.getRole().getName());
        mailDto.setCooperationName(coop.getName());
        mailDto.setType(InvitationTypeDto.COOPERATION);
        return mailDto;
    }

    @Override
    protected InvitationService<CooperationInvitation, CooperationInvitationDto> getMailableService() {
        return invitationService;
    }
}
