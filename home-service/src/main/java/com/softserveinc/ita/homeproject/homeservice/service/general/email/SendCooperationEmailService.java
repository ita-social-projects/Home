package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component

public class SendCooperationEmailService extends BaseEmailService<CooperationInvitation, CooperationInvitationDto> {

    private static final String NOT_FOUND_COOPERATION_FORMAT = "Can't find cooperation with given ID: %d";

    private final InvitationService<CooperationInvitation, CooperationInvitationDto> invitationService;

    private final CooperationRepository cooperationRepository;

    public SendCooperationEmailService(
        @Qualifier(value = "cooperationInvitationServiceImpl")
            InvitationService<CooperationInvitation, CooperationInvitationDto> invitationService,
        CooperationRepository cooperationRepository) {
        this.invitationService = invitationService;
        this.cooperationRepository = cooperationRepository;
    }

    @Override
    protected MailDto createBaseMailDto(InvitationDto invitationDto) {
        CooperationInvitationDto invitation = mapper.convert(invitationDto, CooperationInvitationDto.class);
        Cooperation coop = cooperationRepository.findById(invitation.getCooperationId())
            .filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(
                String.format(NOT_FOUND_COOPERATION_FORMAT, invitation.getCooperationId())));
        MailDto mailDto = MailDto.builder()
            .role(invitation.getRole().getName())
            .cooperationName(coop.getName())
            .type(InvitationTypeDto.COOPERATION).build();
        return mailDto;
    }


    @Override
    protected InvitationService<CooperationInvitation, CooperationInvitationDto> getInvitationService() {
        return invitationService;
    }
}
