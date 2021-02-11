package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;

public interface TemplateService {
    String createMessageTextFromTemplate(MailDto mailDto);
}
