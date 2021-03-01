package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ContactDto extends BaseDto {

    private ContactType contactType;

    private Boolean main;
}
