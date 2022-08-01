package com.softserveinc.ita.homeproject.homeservice.service.general.mail;

import java.time.LocalDateTime;
import javax.activation.DataHandler;
import javax.mail.MessagingException;

import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;

public interface MailService {

    /**
        For properly working usage of this functionality most of the symbols in
        the sending letter should contain more non-ascii symbols instead of ascii.
        See {@link javax.mail.internet.MimeUtility#getEncoding(DataHandler)}
     */
    LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException;
}
