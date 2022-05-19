package com.softserveinc.ita.homeproject.homeoauthserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokenDto {

    private String accessToken;

    private String refreshToken;
}
