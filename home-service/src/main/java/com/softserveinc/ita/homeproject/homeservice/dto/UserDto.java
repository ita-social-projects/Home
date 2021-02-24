package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class UserDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private List<ContactDto> contacts;
}
