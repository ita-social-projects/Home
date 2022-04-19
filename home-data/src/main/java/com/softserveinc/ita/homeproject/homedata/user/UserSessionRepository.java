package com.softserveinc.ita.homeproject.homedata.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    boolean existsUserSessionByAccessToken(String accessToken);

}
