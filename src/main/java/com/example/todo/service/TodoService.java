package com.example.todo.service;

import com.example.todo.domain.Todo;
import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoInfo createTodo(CreateTodoRequest req) {
        Todo todo = todoRepository.save(req.toEntity(getUsernameAtToken()));
        return TodoInfo.of(todo);
    }

    private String getUsernameAtToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public TodoInfo getTodoInfo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(NotExistException::new);
        return TodoInfo.of(todo);
    }

    public Map<String, List<TodoInfo>> getTodoInfoList() {
        return todoRepository.findAll().stream()
                .map(TodoInfo::of)
                .sorted(comparing(TodoInfo::createdAt).reversed())
                .collect(groupingBy(TodoInfo::author));
    }
}
