package com.softserveinc.ita.homeproject.homeoauthserver.service;

import com.softserveinc.ita.homeproject.homeoauthserver.dto.AccessTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.CreateTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.RefreshTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserCredentialsDto;

public interface OauthService {

    CreateTokenDto generateToken(UserCredentialsDto userCredentialsDto);

    AccessTokenDto updateToken(RefreshTokenDto refreshTokenDto);
}
