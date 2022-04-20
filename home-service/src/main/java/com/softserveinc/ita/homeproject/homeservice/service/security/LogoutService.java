package com.softserveinc.ita.homeproject.homeservice.service.security;

import com.sun.mail.iap.Response;

public interface LogoutService {

    Response logout();

    Response logoutAll();

}
