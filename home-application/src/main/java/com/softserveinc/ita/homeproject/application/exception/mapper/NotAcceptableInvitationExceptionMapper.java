package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableInvitationException;



@Provider
public class NotAcceptableInvitationExceptionMapper
        extends BaseInvitationExceptionMapper<NotAcceptableInvitationException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }
}
