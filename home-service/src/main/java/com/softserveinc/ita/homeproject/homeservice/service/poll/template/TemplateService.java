package com.softserveinc.ita.homeproject.homeservice.service.poll.template;

import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;

public interface TemplateService {
    String createMessageTextFromTemplate(MailDto mailDto);
}
