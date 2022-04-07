package com.softserveinc.ita.homeproject.homeoauthserver.service;


import com.softserveinc.ita.homeproject.homeoauthserver.config.JwtProvider;
import com.softserveinc.ita.homeproject.homeoauthserver.data.AccessToken;
import com.softserveinc.ita.homeproject.homeoauthserver.data.Token;
import com.softserveinc.ita.homeproject.homeoauthserver.data.UserCredentials;

import com.softserveinc.ita.homeproject.homeoauthserver.data.UserCredentialsRepository;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.RefreshTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserDetailsDto;
import com.softserveinc.ita.homeproject.homeoauthserver.exception.BadRequestOauthException;
import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotFoundOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class OauthServiceImpl implements OauthService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    private static final String USER_NOT_FOUND_FORMAT = "User is not found";

    @Override
    public Token generateToken(UserDetailsDto userDetailsDto) {
        UserCredentials userCredentials =
            findByEmailAndPassword(userDetailsDto.getEmail(), userDetailsDto.getPassword());
        String accessToken = jwtProvider.generateToken(userCredentials.getId(), userCredentials.getEmail(), 1);
        String refreshToken = jwtProvider.generateToken(userCredentials.getId(), userCredentials.getEmail(), 7);
        return new Token(accessToken, refreshToken);
    }

    @Override
    public AccessToken updateToken(RefreshTokenDto refreshTokenDto) {
        jwtProvider.validateToken(refreshTokenDto.getRefreshToken());
        UserCredentials userCredentials =
            userCredentialsRepository.findByEmail(jwtProvider.getEmailFromToken(refreshTokenDto.getRefreshToken()))
                .filter(UserCredentials::getEnabled)
                .orElseThrow(() -> new NotFoundOauthException(USER_NOT_FOUND_FORMAT));
        String accessToken = jwtProvider.generateToken(userCredentials.getId(), userCredentials.getEmail(), 1);
        return new AccessToken(accessToken);
    }

    private UserCredentials findByEmailAndPassword(String email, String password) {

        UserCredentials userCredentials =
            userCredentialsRepository.findByEmail(email).filter(UserCredentials::getEnabled)
                .orElseThrow(() -> new NotFoundOauthException(USER_NOT_FOUND_FORMAT));

        if (BCrypt.checkpw(password, userCredentials.getPassword())) {
            return userCredentials;
        }

        throw new BadRequestOauthException(USER_NOT_FOUND_FORMAT);
    }
}
