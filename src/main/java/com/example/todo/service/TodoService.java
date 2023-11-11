package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Todo;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.UpdateTodoRequest;
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
        Todo todo = todoRepository.save(req.toEntity(getLoginCustomerName()));
        return TodoInfo.of(todo);
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

    public TodoInfo updateTodo(UpdateTodoRequest request) {
        Todo todo = todoRepository.findByIdAndAuthor(request.id(), getLoginCustomerName())
                .orElseThrow(() -> new NotExistException("존재하지 않은 할일 목록입니다."));

        todo.update(request);
        return TodoInfo.of(todo);
    }

    private String getLoginCustomerName() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
