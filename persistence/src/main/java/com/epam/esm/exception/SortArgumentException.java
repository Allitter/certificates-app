package com.epam.esm.exception;

public class SortArgumentException extends RuntimeException {
    private final String argName;

    public SortArgumentException(String argName) {
        this.argName = argName;
    }

    public String getArgName() {
        return argName;
    }
}
