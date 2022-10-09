package com.example.demo.Exception;

public class DifficultiesIsNotValidException extends RuntimeException {
    public DifficultiesIsNotValidException(String error) {
        super(error);
    }
}