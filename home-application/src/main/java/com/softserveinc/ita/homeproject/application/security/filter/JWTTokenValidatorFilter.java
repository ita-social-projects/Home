package com.softserveinc.ita.homeproject.application.security.filter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softserveinc.ita.homeproject.application.security.exception.NotAcceptableOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Autowired
    public JWTTokenValidatorFilter(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.contains("Bearer ")) {
            token = token.substring(7);
        }

        try {
            jwtProvider.validate(token);
            String username = jwtProvider.getUsername(token);
            Authentication authentication = jwtProvider.getAuthentication(username);

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (NotAcceptableOauthException exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setHeader("message", exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        AtomicBoolean result = new AtomicBoolean(false);
        Map<String, String> urisWithHttpMethod = Map.of(
            "/api/*/users", HttpMethod.POST.name(),
            "/api/*/cooperations", HttpMethod.POST.name(),
            "/api/*/apidocs/**", "",
            "/api/*/version.json", ""
        );

        urisWithHttpMethod.keySet().stream()
            .filter(s -> new AntPathMatcher().match(s, request.getServletPath()))
            .forEach(s -> {
                if (urisWithHttpMethod.get(s).equals("") || urisWithHttpMethod.get(s).equals(request.getMethod())) {
                    result.set(true);
                }
            });

        return result.get();
    }


}
