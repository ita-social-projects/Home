package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import java.math.BigDecimal;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment.ApartmentInvitationService;
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
        mailDto.setRegistrationToken(invitation.getRegistrationToken());
        mailDto.setApartmentNumber(invitation.getApartmentNumber());
        mailDto.setOwnershipPat(BigDecimal.ZERO);
        checkRegistration(invitation, mailDto);
        return mailDto;
    }
}
