package com.softserveinc.ita.homeproject.homeservice.util;

import org.springframework.stereotype.Component;

@Component
public class ValidationHelper {

    /**
     * ^                   # start-of-string
     * (?=.*\d)            # a digit must occur at least once
     * (?=.*[a-z])         # a lower case letter must occur at least once
     * (?=.*[A-Z])         # an upper case letter must occur at least once
     * (?=\S+$)            # no whitespace allowed in the entire string
     * (?=\s*\S).*         # empty password not allowed
     * .{8,}               # anything, at least eight places though
     * $                   # end-of-string
     */
    private static final String PASSWORD_VALIDATION_REGEX
            = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$)(?=\\s*\\S).*.{8,}$";

    public boolean validatePasswordComplexity(String password) {
        return password != null && password.matches(PASSWORD_VALIDATION_REGEX);
    }

    public boolean validatePasswordConfirmation(String password, String passwordConfirmation) {
        return password != null && password.equals(passwordConfirmation);
    }
}
