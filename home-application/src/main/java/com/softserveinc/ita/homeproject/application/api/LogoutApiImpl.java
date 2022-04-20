package com.softserveinc.ita.homeproject.application.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LogoutApiImpl extends CommonApi implements LogoutApi {

    @Autowired
    private HttpServletRequest request;

    @Override
    public Response logout() {
        String header = request.getHeader("Authorization");
        return null;
    }

    @Override
    public Response logoutAll() {
        return null;
    }
}
