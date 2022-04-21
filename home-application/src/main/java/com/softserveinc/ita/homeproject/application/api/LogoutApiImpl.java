package com.softserveinc.ita.homeproject.application.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.service.user.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Provider
@Controller
public class LogoutApiImpl extends CommonApi implements LogoutApi {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserSessionService userSessionService;

    @Override
    public Response logout() {
        String token = request.getHeader("Authorization").substring(7);
        userSessionService.logout(token);

        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response logoutAll() {
        userSessionService.logoutAll();

        return Response.status(Response.Status.OK).build();
    }
}
