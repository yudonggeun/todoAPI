package com.example.todo.controller;

import com.example.todo.dto.MessageResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageResponse badRequest(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new MessageResponse("bad request", message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public MessageResponse badRequest(HttpMessageNotReadableException e){
        return new MessageResponse("bad request", "http 형식을 수정해주세요");
    }
}
