package com.example.todo.controller;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageResponse badRequest(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new MessageResponse("bad request", message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageResponse badRequest(HttpMessageNotReadableException e){
        return new MessageResponse("bad request", "http 형식을 수정해주세요");
    }

    @ExceptionHandler({NotExistException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageResponse unauthorized(Exception e){
        return new MessageResponse("unauthorized", e.getMessage());
    }
}
