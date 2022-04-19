package com.softserveinc.ita.homeproject.homeservice.service.user;

public interface UserSessionService {

    boolean validateUserByAccessToken(String accessToken);
}
