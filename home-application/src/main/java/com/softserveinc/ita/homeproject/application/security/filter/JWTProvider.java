package com.softserveinc.ita.homeproject.application.security.filter;

import com.softserveinc.ita.homeproject.application.security.exception.NotAcceptableOauthException;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserSessionService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class JWTProvider {

    private final static String NOT_ACCEPTABLE_TOKEN_MESSAGE = "Token is not correct";

    @Value("${jwt.token.secret}")
    private String secret;

    private final UserSessionService userSessionService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JWTProvider(UserSessionService userSessionService,
                       UserDetailsService userDetailsService) {
        this.userSessionService = userSessionService;
        this.userDetailsService = userDetailsService;
    }

    public void validate(String token) {
        if (!userSessionService.validateUserByAccessToken(token)) {
            throw new NotAcceptableOauthException(NOT_ACCEPTABLE_TOKEN_MESSAGE);
        }
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
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

    String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
            .getSubject();
    }

    Authentication getAuthentication(String username) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
