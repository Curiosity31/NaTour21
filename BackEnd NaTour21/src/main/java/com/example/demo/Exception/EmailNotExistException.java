package com.example.demo.Exception;

public class EmailNotExistException extends RuntimeException {
    public EmailNotExistException(String error) {
        super(error);
    }
}