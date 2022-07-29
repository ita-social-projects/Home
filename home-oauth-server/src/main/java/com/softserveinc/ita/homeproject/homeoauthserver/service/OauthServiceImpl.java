package com.softserveinc.ita.homeproject.homeoauthserver.service;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.BAD_CREDENTIAL_ERROR_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.INVALID_TOKEN_MESSAGE;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserSession;
import com.softserveinc.ita.homeproject.homedata.user.UserSessionRepository;
import com.softserveinc.ita.homeproject.homeoauthserver.config.JwtProvider;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.AccessTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.CreateTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.RefreshTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserCredentialsDto;
import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotAcceptableOauthException;
import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotFoundOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


@Service
public class OauthServiceImpl implements OauthService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public CreateTokenDto generateToken(UserCredentialsDto userCredentialsDto) {
        User user =
            findByEmailAndPassword(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
        CreateTokenDto createTokenDto = new CreateTokenDto();
        UserSession userSession = new UserSession();

        createTokenDto
            .setAccessToken(jwtProvider.generateToken(user.getId(), user.getEmail(), 1));
        createTokenDto
            .setRefreshToken(jwtProvider.generateToken(user.getId(), user.getEmail(), 7));

        userSession.setUserId(user.getId());
        userSession.setAccessToken(createTokenDto.getAccessToken());
        userSession.setRefreshToken(createTokenDto.getRefreshToken());
        userSession.setExpireDate(jwtProvider.getDate());

        userSessionRepository.save(userSession);

        return createTokenDto;
    }

    @Override
    public AccessTokenDto updateToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        UserSession userSession = getUserSession(refreshToken);
        AccessTokenDto accessTokenDto = new AccessTokenDto();

        jwtProvider.validateToken(refreshToken);
        accessTokenDto
            .setAccessToken(
                jwtProvider.generateToken(userSession.getId(), jwtProvider.getEmailFromToken(refreshToken), 1));

        userSession.setAccessToken(accessTokenDto.getAccessToken());

        userSessionRepository.save(userSession);

        return accessTokenDto;
    }

    private User findByEmailAndPassword(String email, String password) {
        User user =
            userRepository.findByEmail(email).filter(User::getEnabled)
                .orElseThrow(() -> new NotFoundOauthException(BAD_CREDENTIAL_ERROR_MESSAGE));

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        throw new NotFoundOauthException(BAD_CREDENTIAL_ERROR_MESSAGE);
    }

    private UserSession getUserSession(String refreshToken) {
        return userSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new NotAcceptableOauthException(INVALID_TOKEN_MESSAGE));
    }
}
