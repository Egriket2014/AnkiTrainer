package com.ankitrainer.exception;

public class AnkiConnectException extends RuntimeException {

    public AnkiConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnkiConnectException(String message) {
        super(message);
    }
}
