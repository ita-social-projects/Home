package com.softserveinc.ita.homeproject.application.security.filter;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JWTProvider implements InitializingBean {
    @Value("${jwt.token.secret}")
    private String secret;

    private String encryptedSecret;

    @Autowired
    private final UserDetailsService userDetailsService;

    public JWTProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        encryptedSecret = Base64.encodeBase64String(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validate(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(encryptedSecret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("Unsupported JWT exception");
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(encryptedSecret).parseClaimsJws(token).getBody()
            .getSubject();
    }

    public Authentication getAuthentication(String username) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
