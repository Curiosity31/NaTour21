package com.example.demo.Exception;

public class EmailAlreadyPresentException extends RuntimeException {
    public EmailAlreadyPresentException(String error) {
        super(error);
    }
}
