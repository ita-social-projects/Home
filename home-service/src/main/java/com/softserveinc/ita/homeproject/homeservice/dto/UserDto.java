package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String contacts;
}
