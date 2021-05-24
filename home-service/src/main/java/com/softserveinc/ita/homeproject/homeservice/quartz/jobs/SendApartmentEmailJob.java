package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class SendApartmentEmailJob{

    private final ServiceMapper mapper;

    private final MailService mailService;

    private final ApartmentInvitationService apartmentInvitationService;


    @SneakyThrows
    protected void executeAllInvitationsByType() {
        List<ApartmentInvitationDto> invitations = apartmentInvitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            apartmentInvitationService.updateSentDateTimeAndStatus(invite.getId());
        }
    }

    protected MailDto createMailDto(InvitationDto invitationDto) {
        var invitation = mapper.convert(invitationDto, ApartmentInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.APARTMENT);
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setLink(createLink());
        mailDto.setApartmentNumber(invitation.getApartmentNumber());
        mailDto.setOwnershipPat(invitation.getOwnershipPart());
        return mailDto;
    }

    protected String createLink() {
        //TODO: generate token link from type
        return "invitationToApartmentLink";
    }
}
