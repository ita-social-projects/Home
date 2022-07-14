package com.softserveinc.ita.homeproject.homeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Password too weak")
public class PasswordException extends RuntimeException { }
