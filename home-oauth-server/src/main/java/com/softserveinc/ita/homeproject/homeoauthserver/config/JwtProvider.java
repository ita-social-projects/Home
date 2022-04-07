package com.softserveinc.ita.homeproject.homeoauthserver.config;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import javax.annotation.PostConstruct;

import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotAcceptableOauthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(Long id, String email, int amountDays) {
        return Jwts.builder()
            .setId(String.valueOf(id))
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(getExpirationDate(amountDays))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    private Date getExpirationDate(int amountDays) {
        LocalDateTime date = LocalDateTime.now().plusDays(amountDays);
        return Date.from(date.atZone(ZoneId.systemDefault())
            .toInstant());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {} ", e);
            throw new NotAcceptableOauthException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e);
            throw new NotAcceptableOauthException("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e);
            throw new NotAcceptableOauthException("Invalid JWT signature");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e);
            throw new NotAcceptableOauthException("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e);
            throw new NotAcceptableOauthException("JWT claims string is empty");
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
