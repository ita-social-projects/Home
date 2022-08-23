package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.user.UserSession;

public interface UserSessionService {

    boolean validateUserByAccessToken(String accessToken);

    void logout(String token);

    void logoutAll();

    UserSession getByAccessToken(String token);
}
