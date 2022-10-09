package com.example.demo.Exception;

public class NickNameNotValidException extends RuntimeException {
    public NickNameNotValidException(String error) {
            super(error);
    }
}
