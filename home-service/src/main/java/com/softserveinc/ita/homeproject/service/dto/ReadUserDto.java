package com.softserveinc.ita.homeproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ReadUserDto {

    private String firstName;

    private String lastName;

    private Long id;

    private String contacts;

    private String email;
}
