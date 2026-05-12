package com.example.SignalApi.exception;

public class SignalNotFoundException extends RuntimeException {

    public SignalNotFoundException(String id) {
        super("Signal not found with id: " + id);
    }
}
