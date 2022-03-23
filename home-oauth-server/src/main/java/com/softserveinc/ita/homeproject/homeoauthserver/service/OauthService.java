package com.softserveinc.ita.homeproject.homeoauthserver.service;

import com.softserveinc.ita.homeproject.homeoauthserver.data.AccessToken;
import com.softserveinc.ita.homeproject.homeoauthserver.data.Token;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.RefreshTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserDetailsDto;

public interface OauthService {

    Token generateToken(UserDetailsDto userDetailsDto);

    AccessToken updateToken(RefreshTokenDto refreshTokenDto);
}
