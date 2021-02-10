package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;

public interface TemplateService {
    String createMessageTextFromTemplate(InvitationDto invitationDto);
}
