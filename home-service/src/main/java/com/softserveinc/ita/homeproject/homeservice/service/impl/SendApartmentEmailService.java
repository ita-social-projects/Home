package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendApartmentEmailService extends BaseEmailService {

    private final ApartmentInvitationService apartmentInvitationService;

    @Override
    @SneakyThrows
    public void executeAllInvitationsByType() {
        List<ApartmentInvitationDto> invitations = apartmentInvitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            apartmentInvitationService.updateSentDateTimeAndStatus(invite.getId());
        }
    }

    @Override
    protected MailDto createMailDto(InvitationDto invitationDto) {
        var invitation = mapper.convert(invitationDto, ApartmentInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.APARTMENT);
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setApartmentNumber(invitation.getApartmentNumber());
        mailDto.setOwnershipPat(invitation.getOwnershipPart());
        checkRegistration(invitation, mailDto);
        return mailDto;
    }
}
