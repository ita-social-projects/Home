package com.softserveinc.ita.homeproject.homeoauthserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDto {
    private String email;

    private String password;
}
