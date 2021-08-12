package com.softserveinc.ita.homeproject.homeservice.service.template;

import com.softserveinc.ita.homeproject.homeservice.dto.mail.MailDto;

public interface TemplateService {
    String createMessageTextFromTemplate(MailDto mailDto);
}
