package com.example.todo.common.exception;

public class NotExistException extends RuntimeException{
    public NotExistException() {
        super();
    }

    public NotExistException(String message) {
        super(message);
    }
}
