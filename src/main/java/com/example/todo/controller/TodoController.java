package com.example.todo.controller;

import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.service.TodoService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(CreateTodoRequest request) {
        var todoInfo = todoService.create(request);
        return ResponseEntity.ok(todoInfo);
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
