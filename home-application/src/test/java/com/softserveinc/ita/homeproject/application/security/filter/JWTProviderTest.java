package com.softserveinc.ita.homeproject.application.security.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.application.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.application.config.HomeUserWrapperDetails;
import com.softserveinc.ita.homeproject.application.security.exception.NotAcceptableOauthException;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserSessionService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestComponent
@SpringBootTest(classes = HomeServiceTestContextConfig.class)
class JWTProviderTest {
    @MockBean
    private UserSessionService userSessionService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private JWTProvider jwtProvider;

    @Test
    void validateShouldThrowNotAcceptableOauthException() {
        String token = "123";
        when(userSessionService.validateUserByAccessToken(token)).thenReturn(false);

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validate(token))
            .withMessageContaining("Token is not correct");
    }

    @Test
    void validateShouldThrowSignatureException() {
        String token = Jwts.builder()
            .setId(String.valueOf(1L))
            .setSubject("test@gmail.com")
            .setIssuedAt(new Date())
            .setExpiration(
                Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, "ne_secret")
            .compact();

        when(userSessionService.validateUserByAccessToken(token)).thenReturn(true);

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validate(token))
            .withMessageContaining("Invalid JWT signature");
    }

    @Test
    void validateShouldExpiredJwtException() {
        String token = Jwts.builder()
            .setId(String.valueOf(1L))
            .setSubject("test@gmail.com")
            .setIssuedAt(new Date())
            .setExpiration(
                Date.from(LocalDateTime.now().minusDays(1L).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, "secret")
            .compact();

        when(userSessionService.validateUserByAccessToken(token)).thenReturn(true);

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validate(token))
            .withMessageContaining("Expired JWT token");
    }

    @Test
    void validateShouldMalformedJwtException() {
        String token = "123";

        when(userSessionService.validateUserByAccessToken(token)).thenReturn(true);

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validate(token))
            .withMessageContaining("Invalid JWT token");
    }

    @Test
    void validateShouldUnsupportedJwtException() {

        String token = Jwts.builder()
            .setId(String.valueOf(1L))
            .setSubject("test@gmail.com")
            .setIssuedAt(new Date())
            .setExpiration(
                Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant()))
            .compact();

        when(userSessionService.validateUserByAccessToken(token)).thenReturn(true);

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validate(token))
            .withMessageContaining("Unsupported JWT token");
    }

    @Test
    void validateShouldIllegalArgumentException() {
        String token = "";

        when(userSessionService.validateUserByAccessToken(token)).thenReturn(true);

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validate(token))
            .withMessageContaining("JWT claims string is empty");
    }

    @Test
    void getUsernameShouldReturnUsername() {
        String token = Jwts.builder()
            .setId(String.valueOf(1L))
            .setSubject("test@gmail.com")
            .setIssuedAt(new Date())
            .setExpiration(
                Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, "secret")
            .compact();

        Assertions.assertEquals("test@gmail.com", jwtProvider.getUsername(token));
    }

    @Test
    void getAuthenticationShouldReturnAuthentication() {
        User user = new User();
        UserDetails userDetails = new HomeUserWrapperDetails(user, List.of("READ_NEWS"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
            "", userDetails.getAuthorities());

        user.setEnabled(true);
        user.setCredentialsExpired(false);
        user.setPassword("test");
        user.setEmail("test@gmail.com");
        user.setLocked(false);
        user.setCredentialsExpired(false);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        assertThat(jwtProvider.getAuthentication(user.getEmail())).usingRecursiveComparison()
            .isEqualTo(authentication);
    }

    @Test
    void getAuthenticationShouldThrowUsernameNotFoundException() {
        User user = new User();
        UserDetails userDetails = new HomeUserWrapperDetails(user, List.of("READ_NEWS"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
            "", userDetails.getAuthorities());

        user.setEnabled(true);
        user.setCredentialsExpired(false);
        user.setPassword("test");
        user.setEmail("test@gmail.com");
        user.setLocked(false);
        user.setCredentialsExpired(false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> jwtProvider.getAuthentication("test@gmail.com"));
    }
}