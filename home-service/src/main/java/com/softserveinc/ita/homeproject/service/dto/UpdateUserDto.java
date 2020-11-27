package com.softserveinc.ita.homeproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UpdateUserDto {

    private String firstName;

    private String lastName;

    private String contacts;
}
