package com.softserveinc.ita.homeproject.homeoauthserver.config;

import javax.persistence.SecondaryTable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotAcceptableOauthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Getter
    @Setter
    private LocalDateTime date;

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
        date = LocalDateTime.now().plusDays(amountDays);
        return Date.from(date.atZone(ZoneId.systemDefault())
            .toInstant());
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {} ", e);
            throw new NotAcceptableOauthException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e);
            throw new NotAcceptableOauthException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e);
            throw new NotAcceptableOauthException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e);
            throw new NotAcceptableOauthException("Unsupported JWT token");
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

