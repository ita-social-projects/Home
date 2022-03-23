package com.softserveinc.ita.homeproject.homeoauthserver.controller;

import com.softserveinc.ita.homeproject.homeoauthserver.data.AccessToken;
import com.softserveinc.ita.homeproject.homeoauthserver.data.Token;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.RefreshTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserDetailsDto;
import com.softserveinc.ita.homeproject.homeoauthserver.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    @Autowired
    private OauthService oauthService;

    @PostMapping("/login")
    public ResponseEntity<Token> getToken(@RequestBody UserDetailsDto userDetailsDto) {
       Token token = oauthService.generateToken(userDetailsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessToken> getToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        AccessToken accessToken = oauthService.updateToken(refreshTokenDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(accessToken);
    }
}
