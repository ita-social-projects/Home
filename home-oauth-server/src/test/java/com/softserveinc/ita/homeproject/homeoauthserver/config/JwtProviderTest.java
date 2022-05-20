package com.softserveinc.ita.homeproject.homeoauthserver.config;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import com.softserveinc.ita.homeproject.homeoauthserver.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotAcceptableOauthException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@SpringBootTest(classes = HomeServiceTestContextConfig.class)
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider ;

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

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validateToken(token))
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

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validateToken(token))
            .withMessageContaining("Expired JWT token");
    }

    @Test
    void validateShouldMalformedJwtException() {
        String token = "123";

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validateToken(token))
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

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validateToken(token))
            .withMessageContaining("Unsupported JWT token");
    }

    @Test
    void validateShouldIllegalArgumentException() {
        String token = "";

        assertThatExceptionOfType(NotAcceptableOauthException.class)
            .isThrownBy(() -> jwtProvider.validateToken(token))
            .withMessageContaining("JWT claims string is empty");
    }

    @Test
    void getEmailShouldReturnEmail() {
        String token = Jwts.builder()
            .setId(String.valueOf(1L))
            .setSubject("test@gmail.com")
            .setIssuedAt(new Date())
            .setExpiration(
                Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, "secret")
            .compact();

        Assertions.assertEquals("test@gmail.com", jwtProvider.getEmailFromToken(token));
    }
}
