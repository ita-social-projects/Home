package com.softserveinc.ita.homeproject.homeoauthserver.controller;

import com.softserveinc.ita.homeproject.homeoauthserver.api.ServerApi;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.AccessTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.CreateTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.RefreshTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserCredentialsDto;
import com.softserveinc.ita.homeproject.homeoauthserver.model.AccessToken;
import com.softserveinc.ita.homeproject.homeoauthserver.model.CreateToken;
import com.softserveinc.ita.homeproject.homeoauthserver.model.RefreshToken;
import com.softserveinc.ita.homeproject.homeoauthserver.model.UserCredentials;
import com.softserveinc.ita.homeproject.homeoauthserver.service.OauthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/0/oauth2")
public class OauthController implements ServerApi {

    @Autowired
    private OauthService oauthService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<CreateToken> authenticateUser(UserCredentials userCredentials) {
        CreateTokenDto createTokenDto =
            oauthService.generateToken(modelMapper.map(userCredentials, UserCredentialsDto.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createTokenDto, CreateToken.class));
    }

    @Override
    public ResponseEntity<AccessToken> updateAccessToken(RefreshToken refreshToken) {
        AccessTokenDto accessTokenDto = oauthService.updateToken(modelMapper.map(refreshToken, RefreshTokenDto.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(accessTokenDto, AccessToken.class));
    }
}

