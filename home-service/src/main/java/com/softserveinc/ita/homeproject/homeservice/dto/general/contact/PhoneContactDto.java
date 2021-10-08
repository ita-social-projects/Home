package com.softserveinc.ita.homeproject.homeservice.dto.general.contact;

import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneContactDto extends ContactDto {

    private String phone;
}
