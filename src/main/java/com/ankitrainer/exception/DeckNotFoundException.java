package com.ankitrainer.exception;

public class DeckNotFoundException extends RuntimeException {

    public DeckNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeckNotFoundException(String message) {
        super(message);
    }
}
