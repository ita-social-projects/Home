package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.user.UserCredentials;
import com.softserveinc.ita.homeproject.homedata.user.UserCredentialsRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserSessionRepository;
import com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    public UserSessionServiceImpl(
        UserSessionRepository userSessionRepository,
        UserCredentialsRepository userCredentialsRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public boolean validateUserByAccessToken(String accessToken) {
        return userSessionRepository.existsUserSessionByAccessToken(accessToken);
    }

    private UserCredentials getUserFromSecurityContext() {
        return userCredentialsRepository.findByEmail(
            SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
                () -> new NotFoundHomeException(ExceptionMessages.NOT_FOUND_CURRENT_USER_MESSAGE)
        );
    }

    @Override
    @Transactional
    public void logout(String token) {
        userSessionRepository.deleteByAccessToken(token);
    }

    @Override
    @Transactional
    public void logoutAll() {
        userSessionRepository.deleteByUserId(getUserFromSecurityContext().getId());
    }
}
