package com.softserveinc.ita.homeproject.homeservice.service.email;

import com.softserveinc.ita.homeproject.homeservice.dto.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.mail.MailDto;

public interface EmailService {
    void executeAllInvitationsByType();

    MailDto createMailDto(InvitationDto invitationDto);

    void checkRegistration(InvitationDto invitationDto, MailDto mailDto);
}
