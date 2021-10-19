package com.softserveinc.ita.homeproject.homeservice.dto.user;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto {

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String password;

    private List<ContactDto> contacts;

    private String registrationToken;
}
