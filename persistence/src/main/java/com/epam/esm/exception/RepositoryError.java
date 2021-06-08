package com.epam.esm.exception;

public class RepositoryError extends RuntimeException {

    public RepositoryError() {
    }

    public RepositoryError(String message) {
        super(message);
    }

    public RepositoryError(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryError(Throwable cause) {
        super(cause);
    }
}
