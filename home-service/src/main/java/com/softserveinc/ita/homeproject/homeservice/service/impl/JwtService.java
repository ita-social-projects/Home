package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import com.softserveinc.ita.homeproject.homeservice.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;


@Service
public class JwtService implements TokenService {
    private final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    @Override
    public String generateToken(String login) {
        return Jwts.builder()
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .setSubject(login)
                .setIssuer("identity")
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
                .setIssuedAt(Date.from(Instant.now()))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPrivate())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException
                | UnsupportedJwtException
                | MalformedJwtException
                | SignatureException
                | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getIdFromToken(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPrivate())
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
