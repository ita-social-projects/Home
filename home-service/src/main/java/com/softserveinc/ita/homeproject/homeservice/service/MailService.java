package com.softserveinc.ita.homeproject.homeservice.service;

import java.time.LocalDateTime;
import javax.mail.MessagingException;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;

public interface MailService {

    LocalDateTime sendTextMessage(InvitationDto invitationDto) throws MessagingException;
}
