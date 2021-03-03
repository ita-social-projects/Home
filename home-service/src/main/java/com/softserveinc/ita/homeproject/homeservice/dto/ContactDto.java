package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ContactDto extends BaseDto {

    private ContactTypeDto type;

    private Boolean main;
}
