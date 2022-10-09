package com.example.demo.Exception;

public class IdNotExistException extends RuntimeException {
    public IdNotExistException(String error) {
        super(error);
    }
}