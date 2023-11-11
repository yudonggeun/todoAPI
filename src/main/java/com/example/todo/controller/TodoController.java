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
        var todoInfo = todoService.createTodo(request);
        return ResponseEntity.ok(todoInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodo(@PathParam("id") Long id) {
        var todoInfo = todoService.getTodoInfo(id);
        return ResponseEntity.ok(todoInfo);
    }

    @GetMapping
    public ResponseEntity<?> getTodoList() {
        return ResponseEntity.ok(todoService.getTodoInfoList());
    }

    @PatchMapping
    public ResponseEntity<?> updateTodo() {
        return null;
    }
}
