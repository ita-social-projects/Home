package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserSession;
import com.softserveinc.ita.homeproject.homedata.user.UserSessionRepository;
import com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages;
import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    private final UserRepository userRepository;

    public UserSessionServiceImpl(
        UserSessionRepository userSessionRepository,
        UserRepository userRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean validateUserByAccessToken(String accessToken) {
        return userSessionRepository.existsUserSessionByAccessToken(accessToken);
    }

    private User getUserFromSecurityContext() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
            () -> new UsernameNotFoundException("User not found")
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

    @Override
    public UserSession getByAccessToken(String token) {
        return userSessionRepository.findByAccessToken(token)
                .orElseThrow(() -> new NotAcceptableHomeException(ExceptionMessages.NOT_ACCEPTABLE_TOKEN_MESSAGE));
    }
}
