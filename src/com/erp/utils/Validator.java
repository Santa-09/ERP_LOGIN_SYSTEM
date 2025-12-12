package com.erp.utils;

import java.util.regex.Pattern;

public class Validator {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10}$");
    
    private static final Pattern ROLL_NUMBER_PATTERN = 
        Pattern.compile("^[A-Z]{2}[0-9]{7}$");

    /**
     * Validates if the string is not null or empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates phone number (10 digits)
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates roll number format (e.g., CS2021001)
     */
    public static boolean isValidRollNumber(String rollNumber) {
        if (!isNotEmpty(rollNumber)) {
            return false;
        }
        return ROLL_NUMBER_PATTERN.matcher(rollNumber).matches();
    }

    /**
     * Validates password strength (minimum 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.length() >= 6;
    }

    /**
     * Validates numeric input
     */
    public static boolean isNumeric(String value) {
        if (!isNotEmpty(value)) {
            return false;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates positive integer
     */
    public static boolean isPositiveInteger(String value) {
        if (!isNumeric(value)) {
            return false;
        }
        try {
            int num = Integer.parseInt(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates semester (1-8)
     */
    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 8;
    }

    /**
     * Validates string length
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (!isNotEmpty(value)) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    private Validator() {
        // Private constructor to prevent instantiation
    }
}