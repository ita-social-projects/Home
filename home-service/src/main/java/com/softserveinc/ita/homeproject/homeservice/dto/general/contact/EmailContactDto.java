package com.softserveinc.ita.homeproject.homeservice.dto.general.contact;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmailContactDto extends ContactDto {

    private String email;
}
