package com.example.demo.Exception;



public class ListPointNotExistException extends RuntimeException {
    public ListPointNotExistException(String error) {
        super(error);
    }
}
