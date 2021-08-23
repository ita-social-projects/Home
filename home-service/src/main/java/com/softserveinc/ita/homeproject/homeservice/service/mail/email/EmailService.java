package com.softserveinc.ita.homeproject.homeservice.service.mail.email;

import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.mail.MailDto;

public interface EmailService {
    void executeAllInvitationsByType();

    MailDto createMailDto(InvitationDto invitationDto);

    void checkRegistration(InvitationDto invitationDto, MailDto mailDto);
}
