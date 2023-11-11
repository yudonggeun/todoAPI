package com.example.todo.service;

import com.example.todo.domain.Todo;
import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoInfo create(CreateTodoRequest req) {
        Todo todo = todoRepository.save(req.toEntity(getUsernameAtToken()));
        return TodoInfo.of(todo);
    }

    private String getUsernameAtToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
