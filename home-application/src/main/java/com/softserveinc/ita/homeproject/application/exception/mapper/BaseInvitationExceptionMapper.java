package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;

public abstract class BaseInvitationExceptionMapper<T extends InvitationException> extends BaseExceptionMapper<T> {

    @Override
    protected String extractMessage(T exception) {
        return exception.getMessage();
    }
}
