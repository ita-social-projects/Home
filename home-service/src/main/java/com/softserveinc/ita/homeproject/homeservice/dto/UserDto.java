package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String contacts;
}
