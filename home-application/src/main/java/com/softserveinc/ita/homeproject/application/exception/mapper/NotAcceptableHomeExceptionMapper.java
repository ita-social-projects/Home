package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableHomeException;



@Provider
public class NotAcceptableHomeExceptionMapper
        extends BaseInvitationExceptionMapper<NotAcceptableHomeException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }
}
