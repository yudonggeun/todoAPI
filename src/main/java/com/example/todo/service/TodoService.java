package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoSearchParam;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.dto.TodoInfoEntry;
import com.example.todo.dto.response.TodoInfoListResponse;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

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
        checkLoginCustomerEqualAuthorOfTodo(entity, "작성자만 조회할 수 있습니다.");
        return TodoInfo.of(entity);
    }

    public TodoInfoListResponse getTodoInfoList(TodoSearchParam condition) {

        Map<String, List<TodoInfo>> data = todoRepository
                .findAllByIsCompleteAndCondition(false, condition).stream()
                .map(this::hidePrivateColumn)
                .sorted(byCreatedAtDesc())
                .collect(groupByAuthor());

        var entries = new ArrayList<TodoInfoEntry>(data.size());

        for (var author : data.keySet()) {
            var todoInfoList = data.get(author);
            entries.add(new TodoInfoEntry(author, todoInfoList));
        }

        return new TodoInfoListResponse(entries.size(), entries);
    }

    private Collector<TodoInfo, ?, Map<String, List<TodoInfo>>> groupByAuthor() {
        return groupingBy(TodoInfo::author);
    }

    private Comparator<TodoInfo> byCreatedAtDesc() {
        return comparing(TodoInfo::createdAt).reversed();
    }

    private TodoInfo hidePrivateColumn(TodoInfo todo) {
        if (!todo.author().equals(getLoginCustomerName())) {
            return new TodoInfo(todo.id(), todo.author(), todo.title(), null, todo.createdAt());
        }
        return todo;
    }

    public TodoInfo updateTodo(UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(request.id())
                .orElseThrow(() -> new NotExistException("존재하지 않은 할일 목록입니다."));

        checkLoginCustomerEqualAuthorOfTodo(todo, "작성자만 삭제/수정할 수 있습니다.");
        todo.update(request);
        return TodoInfo.of(todo);
    }

    public void complete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotExistException("존재하지 않은 할일 목록입니다."));

        checkLoginCustomerEqualAuthorOfTodo(todo, "작성자만 삭제/수정할 수 있습니다.");
        todo.complete();
    }

    private void checkLoginCustomerEqualAuthorOfTodo(Todo todo, String errorMessage) {
        if (!todo.getAuthor().equals(getLoginCustomerName())) {
            throw new AccessDeniedException(errorMessage);
        }
    }

    private String getLoginCustomerName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
