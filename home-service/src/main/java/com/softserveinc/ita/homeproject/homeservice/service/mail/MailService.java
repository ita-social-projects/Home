package com.softserveinc.ita.homeproject.homeservice.service.mail;

import java.time.LocalDateTime;
import javax.mail.MessagingException;

import com.softserveinc.ita.homeproject.homeservice.dto.mail.MailDto;

public interface MailService {
    LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException;
}
