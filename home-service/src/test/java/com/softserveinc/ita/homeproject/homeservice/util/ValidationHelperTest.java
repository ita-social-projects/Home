package com.softserveinc.ita.homeproject.homeservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationHelperTest {

    private ValidationHelper validationHelper;

    @BeforeEach
    public void init() {
        validationHelper = new ValidationHelper();
    }

    @Test
    void testWithValidPassword() {
        String password = "Ab1$1234";
        assertTrue(validationHelper.validatePasswordComplexity(password));
    }

    @Test
    void testWithValidPasswordWithLengthMoreThan8() {
        String password = "Ab1$1234678";
        assertTrue(validationHelper.validatePasswordComplexity(password));
    }

    @Test
    void testWithMissingUpperCaseLetter() {
        String password = "ab1$1234";
        assertFalse(validationHelper.validatePasswordComplexity(password));
    }

    @Test
    void testWithMissingLowerCaseLetter() {
        String password = "AB1$1234";
        assertFalse(validationHelper.validatePasswordComplexity(password));
    }

    @Test
    void testWithMissingSpecialLetter() {
        String password = "AB123456";
        assertFalse(validationHelper.validatePasswordComplexity(password));
    }

    @Test
    void testWithPasswordLengthLesserThan8() {
        String password = "Ab1$123";
        assertFalse(validationHelper.validatePasswordComplexity(password));
    }

    @Test
    void testWithValidPasswordUsingSpecialCharacters() {
        char[] specialCharacters = "!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~".toCharArray();
        assertEquals(31, specialCharacters.length);
        for (char specialCharacter : specialCharacters) {
            String password = "Ab12345" + specialCharacter;
            assertTrue(validationHelper.validatePasswordComplexity(password));
        }
    }
}
