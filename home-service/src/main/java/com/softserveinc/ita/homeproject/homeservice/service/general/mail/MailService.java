package com.softserveinc.ita.homeproject.homeservice.service.general.mail;

import java.time.LocalDateTime;
import javax.mail.MessagingException;

import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;

public interface MailService {
    LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException;
}
