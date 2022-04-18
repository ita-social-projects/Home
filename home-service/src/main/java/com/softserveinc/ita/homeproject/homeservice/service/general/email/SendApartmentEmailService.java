package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import java.math.BigDecimal;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component

public class SendApartmentEmailService extends BaseEmailService<ApartmentInvitation, ApartmentInvitationDto> {

    private final InvitationService<ApartmentInvitation, ApartmentInvitationDto> invitationService;

    public SendApartmentEmailService(
        @Qualifier(value = "apartmentInvitationServiceImpl")
            InvitationService<ApartmentInvitation, ApartmentInvitationDto> invitationService) {
        this.invitationService = invitationService;
    }

    @Override
    protected MailDto createBaseMailDto(InvitationDto invitationDto) {
        ApartmentInvitationDto invitation = mapper.convert(invitationDto, ApartmentInvitationDto.class);
        MailDto mailDto = MailDto.builder()
            .type(InvitationTypeDto.APARTMENT)
            .apartmentNumber(invitation.getApartmentNumber())
            .ownershipPat(BigDecimal.ZERO).build();
        return mailDto;
    }

    @Override
    protected InvitationService<ApartmentInvitation, ApartmentInvitationDto> getInvitationService() {
        return invitationService;
    }
}
