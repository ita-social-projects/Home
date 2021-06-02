package com.softserveinc.ita.homeproject.homeservice.service;

import java.time.LocalDateTime;
import javax.mail.MessagingException;

import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;

public interface MailService {
    LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException;
}
