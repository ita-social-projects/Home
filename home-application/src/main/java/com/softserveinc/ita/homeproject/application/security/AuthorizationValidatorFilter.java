package com.softserveinc.ita.homeproject.application.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softserveinc.ita.homeproject.homedata.user.UserSession;
import com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages;
import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;


public class AuthorizationValidatorFilter extends OncePerRequestFilter {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserCooperationService userCooperationService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.contains("Bearer ")) {
            token = token.substring(7);
        }

        try {
            UserSession accessToken = userSessionService.getByAccessToken(token);
            userCooperationService.getUserCooperationByUserId(accessToken.getUserId());

        } catch (NotAcceptableHomeException exception) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            response.setHeader("message", exception.getMessage());

        } catch (NotFoundHomeException exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setHeader("message", exception.getMessage());
        } catch (RuntimeException exception) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setHeader("message", ExceptionMessages.SERVER_ERROR_MESSAGE);
        }

        filterChain.doFilter(request, response);
    }
}
