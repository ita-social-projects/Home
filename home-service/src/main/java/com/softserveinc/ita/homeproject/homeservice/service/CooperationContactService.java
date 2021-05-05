package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;

public interface CooperationContactService extends ContactService {

    ContactDto getContactById(Long id);
}
