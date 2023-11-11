package com.example.todo.controller;

import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.dto.response.MessageResponse;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
    public ResponseEntity<?> updateTodo(@Valid @RequestBody UpdateTodoRequest request) {
        TodoInfo info = todoService.updateTodo(request);
        return ResponseEntity.ok(info);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> completeTodo(@PathParam("id") Long id){
        todoService.complete(id);
        return ResponseEntity.ok(new MessageResponse("success", "할일 완료"));
    }
}
