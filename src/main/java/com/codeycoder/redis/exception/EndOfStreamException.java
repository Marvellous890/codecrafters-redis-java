package com.codeycoder.redis.exception;

public class EndOfStreamException extends RuntimeException {
    public EndOfStreamException() {
    }

    public EndOfStreamException(String message) {
        super(message);
    }

    public EndOfStreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
