package com.softserveinc.ita.homeproject.homeoauthserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsDto {
    private String email;

    private String password;
}
