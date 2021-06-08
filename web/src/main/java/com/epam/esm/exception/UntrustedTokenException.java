package com.epam.esm.exception;

public class UntrustedTokenException extends RuntimeException {
    public UntrustedTokenException() {
    }

    public UntrustedTokenException(String message) {
        super(message);
    }

    public UntrustedTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UntrustedTokenException(Throwable cause) {
        super(cause);
    }
}
