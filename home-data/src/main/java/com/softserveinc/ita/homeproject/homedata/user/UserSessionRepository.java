package com.softserveinc.ita.homeproject.homedata.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    boolean existsUserSessionByAccessToken(String accessToken);

    void deleteByAccessToken(String accessToken);

    void deleteByUserId(Long userId);

    List<UserSession> findAllByUserId(Long userId);

    Optional<UserSession> findByRefreshToken(String refreshToken);

}
