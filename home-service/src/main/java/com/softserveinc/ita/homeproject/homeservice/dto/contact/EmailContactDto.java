package com.softserveinc.ita.homeproject.homeservice.dto.contact;

import com.softserveinc.ita.homeproject.homeservice.dto.contact.ContactDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailContactDto extends ContactDto {

    private String email;
}
