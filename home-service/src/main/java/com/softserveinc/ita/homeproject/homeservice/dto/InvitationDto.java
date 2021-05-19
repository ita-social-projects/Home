package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InvitationDto extends BaseDto{

    private InvitationTypeDto type;

    private String email;

}
