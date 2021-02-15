package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.model.ContactType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ContactDto extends BaseDto {

    private Boolean primary;

    private ContactType contactType;

}
