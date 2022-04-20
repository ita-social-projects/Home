package com.softserveinc.ita.homeproject.homeservice.service.security;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserSessionRepository;
import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final UserSessionRepository userSessionRepository;

    @Override
    public Response logout() {
        User user = getUserFromSecurityContext();
        return new Response("");
    }

    @Override
    public Response logoutAll() {
        User user = getUserFromSecurityContext();
        return new Response("");
    }

    private User getUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
