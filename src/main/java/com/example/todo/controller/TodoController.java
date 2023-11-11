package com.example.todo.controller;

import com.example.todo.dto.CreateTodoRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @PostMapping
    public ResponseEntity<?> createTodo(CreateTodoRequest request) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodo(@PathParam("id") Long id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getTodoList() {
        return null;
    }

    @PatchMapping
    public ResponseEntity<?> updateTodo() {
        return null;
    }
}
