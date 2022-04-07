package com.softserveinc.ita.homeproject.application.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProvider jwtProvider;

    public JWTTokenValidatorFilter() {
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.contains("Bearer ")) {
            token = token.substring(7);
        }
        boolean validatedToken = jwtProvider.validate(token);
        if (validatedToken) {
            String username = jwtProvider.getUsername(token);
            Authentication authentication = jwtProvider.getAuthentication(username);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }
}
