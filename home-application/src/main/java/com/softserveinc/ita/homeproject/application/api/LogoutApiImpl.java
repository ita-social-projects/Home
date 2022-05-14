package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_USER;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.service.user.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class LogoutApiImpl extends CommonApi implements LogoutApi {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserSessionService userSessionService;

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response logout() {
        String token = request.getHeader("Authorization").substring(7);
        userSessionService.logout(token);

        return Response.status(Response.Status.OK).build();
    }

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response logoutAll() {
        userSessionService.logoutAll();

        return Response.status(Response.Status.OK).build();
    }
}
