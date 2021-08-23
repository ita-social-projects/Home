package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ContactDto extends BaseDto {

    private ContactTypeDto type;

    private Boolean main;
}
