package com.softserveinc.ita.homeproject.homeservice.service.email;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.invitation.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.invitation.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.invitation.ApartmentInvitationService;
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
    public MailDto createMailDto(InvitationDto invitationDto) {
        var invitation = mapper.convert(invitationDto, ApartmentInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.APARTMENT);
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setRegistrationToken(invitation.getRegistrationToken());
        mailDto.setApartmentNumber(invitation.getApartmentNumber());
        mailDto.setOwnershipPat(invitation.getOwnershipPart());
        checkRegistration(invitation, mailDto);
        return mailDto;
    }
}
