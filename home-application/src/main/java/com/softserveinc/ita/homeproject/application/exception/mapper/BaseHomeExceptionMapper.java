package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseHomeException;

public abstract class BaseHomeExceptionMapper<T extends BaseHomeException> extends BaseExceptionMapper<T> {

    @Override
    protected String extractMessage(T exception) {
        return exception.getMessage();
    }
}
