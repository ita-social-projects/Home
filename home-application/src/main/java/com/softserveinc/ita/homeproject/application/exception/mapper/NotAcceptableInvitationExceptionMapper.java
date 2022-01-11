package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableInvitationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAcceptableInvitationExceptionMapper
        extends BaseInvitationExceptionMapper<NotAcceptableInvitationException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }
}
