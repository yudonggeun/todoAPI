package com.example.todo.common.exception;

public class NotExistException extends RuntimeException{
    public NotExistException() {
        super("요청한 리소스를 찾을 수 없습니다.");
    }

    public NotExistException(String message) {
        super(message);
    }
}
