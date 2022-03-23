package com.softserveinc.ita.homeproject.homeoauthserver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

    private String accessToken;

    private String refreshToken;
}
