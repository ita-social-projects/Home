package com.softserveinc.ita.homeproject.homeservice.dto.general.contact;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.enums.ContactTypeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ContactDto extends BaseDto {

    private ContactTypeDto type;

    private Boolean main;
}
