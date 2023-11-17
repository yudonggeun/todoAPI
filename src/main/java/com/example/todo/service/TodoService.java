package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.TodoInfoEntry;
import com.example.todo.dto.request.TodoSearchParam;
import com.example.todo.dto.TodoShortInfo;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.dto.response.TodoInfoListResponse;
import com.example.todo.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    public TodoInfo createTodo(CreateTodoRequest req) {
        Todo entity = todoRepository.save(req.toEntity(getLoginCustomerName()));
        return TodoInfo.of(entity);
    }

    public TodoInfo getTodoInfo(Long id) {
        Todo entity = todoRepository.findFetchById(id)
                .orElseThrow(NotExistException::new);
        checkLoginCustomerEqualAuthorOfTodo(entity, "작성자만 조회할 수 있습니다.");
        return TodoInfo.of(entity);
    }

    public TodoInfoListResponse getTodoInfoList(TodoSearchParam condition) {

        Map<String, List<TodoShortInfo>> data = todoRepository
                .findAllByIsCompleteAndCondition(false, condition).stream()
                .map(this::hidePrivateColumn)
                // 정렬을 db에서 하지않고 서버에서 수행해서 db의 자원을 절약하고자 사용했는데요. db 부하가 크지 않은 시스템에서는 db에서 하는 것이 좋겠죠?
                .sorted(byCreatedAtDesc())
                .collect(groupByAuthor());

        var entries = new ArrayList<TodoInfoEntry>(data.size());

        for (var author : data.keySet()) {
            var todoInfoList = data.get(author);
            entries.add(new TodoInfoEntry(author, todoInfoList));
        }

        return new TodoInfoListResponse(entries.size(), entries);
    }

    // 조금이라도 가독성을 높이는 것이 좋은가? 해서 분리해서 구현을 했는데요. 이런 부분은 어떤지 궁금합니다.
    private Collector<TodoShortInfo, ?, Map<String, List<TodoShortInfo>>> groupByAuthor() {
        return groupingBy(TodoShortInfo::author);
    }

    private Comparator<TodoShortInfo> byCreatedAtDesc() {
        return comparing(TodoShortInfo::createdAt).reversed();
    }

    private TodoShortInfo hidePrivateColumn(TodoShortInfo todo) {
        if (!todo.author().equals(getLoginCustomerName())) {
            return new TodoShortInfo(todo.id(), todo.author(), todo.title(), null, todo.createdAt());
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

    public void delete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(NotExistException::new);

        checkLoginCustomerEqualAuthorOfTodo(todo, "작성자만 삭제/수정할 수 있습니다.");
        commentRepository.deleteByTodoId(id);
        todoRepository.deleteById(id);
    }

    private void checkLoginCustomerEqualAuthorOfTodo(Todo todo, String errorMessage) {
        if (!todo.getAuthor().equals(getLoginCustomerName())) {
            throw new AccessDeniedException(errorMessage);
        }
    }
    // Authentication 객체를 파라미터로 입력 바아서 인증 정보를 조회하는 방식과
    // context에서 조회해서 가져오는 방식 어떤 것을 사용하는 것이 좋은지 잘 모르겠습니다.

    private String getLoginCustomerName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
