package com.softserveinc.ita.homeproject.homeservice.service;

public interface TokenService {
    String generateToken(String login);

    boolean validateToken(String token);

    String getIdFromToken(String token);
}
