package com.softserveinc.ita.homeproject.homeoauthserver.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import com.softserveinc.ita.homeproject.homeoauthserver.config.JwtProvider;
import com.softserveinc.ita.homeproject.homeoauthserver.data.UserCredentials;
import com.softserveinc.ita.homeproject.homeoauthserver.data.UserCredentialsRepository;
import com.softserveinc.ita.homeproject.homeoauthserver.data.UserSession;
import com.softserveinc.ita.homeproject.homeoauthserver.data.UserSessionRepository;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.CreateTokenDto;
import com.softserveinc.ita.homeproject.homeoauthserver.dto.UserCredentialsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OauthServiceImplIT {

    @Mock
    private static UserSessionRepository userSessionRepository;

    @Mock
    private static JwtProvider jwtProvider;

    @Mock
    private static UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    private OauthServiceImpl oauthService;

    @Test
    void generateTokenShouldReturnTokens() {
        //arrange
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        CreateTokenDto expectedCreateTokenDto = new CreateTokenDto();
        UserCredentials userCredentials = new UserCredentials();
        UserSession userSession = new UserSession();
        userCredentialsDto.setEmail("test@gmail.com");
        userCredentialsDto.setPassword("password");
        expectedCreateTokenDto.setAccessToken("123");
        expectedCreateTokenDto.setRefreshToken("456");
        userCredentials.setId(1000L);
        userCredentials.setEmail("test@gmail.com");
        userCredentials.setPassword("$2a$10$Vb7wd6xqSJ3A8LbHJgb12eeak3ptYgMFPYQNoBlSog.X5eMMXaQK2");
        userCredentials.setEnabled(true);
        userSession.setUserId(userCredentials.getId());
        userSession.setAccessToken(expectedCreateTokenDto.getAccessToken());
        userSession.setRefreshToken(expectedCreateTokenDto.getRefreshToken());
        userSession.setExpireDate(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
        when(userCredentialsRepository.findByEmail("test@gmail.com"))
            .thenReturn(Optional.of(userCredentials));
        when(jwtProvider.generateToken(1000L, "test@gmail.com", 1))
            .thenReturn(("123"));
        when(jwtProvider.generateToken(1000L, "test@gmail.com", 7))
            .thenReturn(("456"));
        when(jwtProvider.getDate()).thenReturn(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
        //act
        CreateTokenDto actualCreateTokenDto = oauthService.generateToken(userCredentialsDto);
        //assert
        assertThat(actualCreateTokenDto).usingRecursiveComparison().isEqualTo(expectedCreateTokenDto);
    }

}