package com.softserveinc.ita.homeproject.homeservice.util;

import org.springframework.stereotype.Component;

@Component
public class ValidationHelper {

    /**
     * ^                   # start-of-string
     * (?=.*[0-9])         # a digit must occur at least once
     * (?=.*[a-z])         # a lower case letter must occur at least once
     * (?=.*[A-Z])         # an upper case letter must occur at least once
     * (?=.*[^a-zA-Z0-9])  # a special character must occur at least once
     * (?=\S+$)            # no whitespace allowed in the entire string
     * .{8,}               # anything, at least eight places though
     * $                   # end-of-string
     */
    private static final String PASSWORD_VALIDATION_REGEX
            = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=\\S+$).{8,}$";

    public boolean validatePasswordComplexity(String password) {
        return password != null && password.matches(PASSWORD_VALIDATION_REGEX);
    }
}
