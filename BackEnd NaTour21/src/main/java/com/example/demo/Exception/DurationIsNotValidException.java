package com.example.demo.Exception;

public class DurationIsNotValidException extends RuntimeException {
    public DurationIsNotValidException(String error) {
        super(error);
    }
}
