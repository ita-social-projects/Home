package com.softserveinc.ita.homeproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ReadUserDto extends BaseUserServiceDto{

    private Long id;

    private List<String> contacts;

    private String email;
}
