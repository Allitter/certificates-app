package com.epam.esm.exception;

import java.util.*;

public class ExceptionResponse {
    private final int code;
    private final List<String> messages;

    public ExceptionResponse(int code, List<String> messages) {
        this.code = code;
        this.messages = new ArrayList<>(messages);
    }

    public ExceptionResponse(int code, String messages) {
        this(code, List.of(messages));
    }

    public int getCode() {
        return code;
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExceptionResponse that = (ExceptionResponse) o;
        return code == that.code && Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, messages);
    }

    @Override
    public String
    toString() {
        return new StringJoiner(", ", ExceptionResponse.class.getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("messages=" + messages)
                .toString();
    }
}
