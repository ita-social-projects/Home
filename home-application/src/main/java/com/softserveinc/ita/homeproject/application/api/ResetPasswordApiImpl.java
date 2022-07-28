package com.softserveinc.ita.homeproject.application.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.PasswordRestorationApproval;
import com.softserveinc.ita.homeproject.application.model.PasswordRestorationRequest;
import com.softserveinc.ita.homeproject.homeservice.dto.user.password.PasswordRestorationApprovalDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.password.PasswordRestorationRequestDto;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Provider
@Component
public class ResetPasswordApiImpl extends CommonApi implements ResetPasswordApi {

    @Autowired
    UserService userService;

    @Override
    public Response passwordRestorationApproval(PasswordRestorationApproval passwordRestorationApproval) {
        PasswordRestorationApprovalDto passwordRestorationApprovalDto =
            mapper.convert(passwordRestorationApproval, PasswordRestorationApprovalDto.class);
        userService.changePassword(passwordRestorationApprovalDto);

        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response passwordRestorationRequest(PasswordRestorationRequest passwordRestorationRequest) {
        PasswordRestorationRequestDto passwordRestorationRequestDto =
            mapper.convert(passwordRestorationRequest, PasswordRestorationRequestDto.class);
        userService.requestPasswordRestoration(passwordRestorationRequestDto);

        return Response.status(Response.Status.CREATED).build();
    }
}
