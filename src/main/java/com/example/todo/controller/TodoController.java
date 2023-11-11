package com.example.todo.controller;

import com.example.todo.dto.TodoSearchParam;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.dto.response.MessageResponse;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody CreateTodoRequest request) {
        return ResponseEntity.ok(todoService.createTodo(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(todoService.getTodoInfo(id));
    }

    @GetMapping
    public ResponseEntity<?> getTodoList(TodoSearchParam condition) {
        return ResponseEntity.ok(todoService.getTodoInfoList(condition));
    }

    @PatchMapping
    public ResponseEntity<?> updateTodo(@Valid @RequestBody UpdateTodoRequest request) {
        TodoInfo info = todoService.updateTodo(request);
        return ResponseEntity.ok(info);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> completeTodo(@PathVariable("id") Long id){
        todoService.complete(id);
        return ResponseEntity.ok(new MessageResponse("success", "할일 완료"));
    }
}
