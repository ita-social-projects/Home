package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import lombok.SneakyThrows;


public class SendApartmentEmailJob extends SendEmailJob{

    private final ApartmentInvitationService invitationService;

    public SendApartmentEmailJob(ServiceMapper mapper,
                                 MailService mailService,
                                 ApartmentInvitationService invitationService) {
        super(mapper, mailService);
        this.invitationService = invitationService;
    }

    @SneakyThrows
    @Override
    protected void executeAllInvitationsByType() {
        List<ApartmentInvitationDto> invitations = invitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            invitationService.updateSentDateTimeAndStatus(invite.getId(), LocalDateTime.now());
        }
    }

    @Override
    protected MailDto createMailDto(InvitationDto invitationDto) {
        var invitation = mapper.convert(invitationDto, ApartmentInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(invitation.getType());
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setLink(createLink());
        mailDto.setApartmentNumber(invitation.getApartmentNumber());
        mailDto.setOwnershipPat(invitation.getOwnershipPart());
        return mailDto;
    }

    @Override
    protected String createLink() {
        //TODO: generate token link from type
        return "invitationToApartmentLink";
    }
}
