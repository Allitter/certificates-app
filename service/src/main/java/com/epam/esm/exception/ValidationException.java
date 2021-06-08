package com.epam.esm.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> validationFails;

    public ValidationException(Map<String, String> validationFails) {
        this.validationFails = new HashMap<>(validationFails);
    }

    public Map<String, String> getValidationFails() {
        return Collections.unmodifiableMap(validationFails);
    }
}
