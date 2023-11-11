package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Todo;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
        Todo entity = todoRepository.save(req.toEntity(getLoginCustomerName()));
        return TodoInfo.of(entity);
    }

    public TodoInfo getTodoInfo(Long id) {
        Todo entity = todoRepository.findById(id)
                .orElseThrow(NotExistException::new);
        checkLoginCustomerEqualAuthorOfTodo(entity);
        return TodoInfo.of(entity);
    }

    public Map<String, List<TodoInfo>> getTodoInfoList() {
        String loginCustomerName = getLoginCustomerName();
        return todoRepository.findAllByIsComplete(false).stream()
                .map(todo -> todo.hidePrivateColumn(loginCustomerName))
                .sorted(comparing(TodoInfo::createdAt).reversed())
                .collect(groupingBy(TodoInfo::author));
    }

    public TodoInfo updateTodo(UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(request.id())
                .orElseThrow(() -> new NotExistException("존재하지 않은 할일 목록입니다."));

        checkLoginCustomerEqualAuthorOfTodo(todo);
        todo.update(request);
        return TodoInfo.of(todo);
    }

    public void complete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotExistException("존재하지 않은 할일 목록입니다."));

        checkLoginCustomerEqualAuthorOfTodo(todo);
        todo.complete();
    }

    private void checkLoginCustomerEqualAuthorOfTodo(Todo todo) {
        if (!todo.getAuthor().equals(getLoginCustomerName())) {
            throw new AccessDeniedException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private String getLoginCustomerName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
