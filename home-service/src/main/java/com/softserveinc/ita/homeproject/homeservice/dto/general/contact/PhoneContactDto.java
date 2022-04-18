package com.softserveinc.ita.homeproject.homeservice.dto.general.contact;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhoneContactDto extends ContactDto {

    private String phone;
}
