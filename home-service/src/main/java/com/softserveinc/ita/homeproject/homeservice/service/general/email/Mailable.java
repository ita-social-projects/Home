package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;


public abstract class Mailable extends BaseDto {

    public abstract String getEmail();

    public abstract String getToken();
}
