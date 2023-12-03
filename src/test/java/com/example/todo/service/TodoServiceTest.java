package com.example.todo.service;

import com.example.todo.TestSupport;
import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.repository.CommentRepository;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("할일 서비스 테스트")
class TodoServiceTest extends TestSupport {

    TodoRepository repository = mock();
    CommentRepository commentRepository = mock();
    LoginStatusService loginStatusService = mock();
    TodoService service = new TodoService(repository, commentRepository, loginStatusService);

    @DisplayName("작성자가 아니라면 할일을 조회할 수 없다.")
    @Test
    void when_other_user_get_todo_then_throw_exception() {
        // given
        var id = 1L;
        var todo = builderFixture.giveMeBuilder(Todo.class)
                .set("author", "author").sample();
        given(repository.findFetchById(1L)).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn("other user");
        // when // then
        thenThrownBy(() -> service.getTodoInfo(id))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("작성자만 조회할 수 있습니다.");
    }

    @DisplayName("작성자라면 할일을 조회할 수 있다.")
    @TestFactory
    Iterable<DynamicTest> dynamic_when_correct_user_get_todo_then_return_info() {
        return List.of(
                dynamicTest("random test", () -> when_correct_user_get_todo_then_return_info(randomString(), randomString(), randomString())),
                dynamicTest("random test", () -> when_correct_user_get_todo_then_return_info(randomString(), randomString(), randomString())),
                dynamicTest("random test", () -> when_correct_user_get_todo_then_return_info(randomString(), randomString(), randomString())),
                dynamicTest("random test", () -> when_correct_user_get_todo_then_return_info(randomString(), randomString(), randomString())),
                dynamicTest("random test", () -> when_correct_user_get_todo_then_return_info(randomString(), randomString(), randomString()))
        );
    }

    void when_correct_user_get_todo_then_return_info(String author, String content, String title) {
        // given
        var id = 1L;
        var todo = Todo.builder()
                .author(author)
                .content(content)
                .title(title)
                .build();

        given(repository.findFetchById(1L)).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn(author);
        // when
        TodoInfo info = service.getTodoInfo(id);
        // then
        then(info.author()).isEqualTo(author);
        then(info.content()).isEqualTo(content);
        then(info.title()).isEqualTo(title);
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    @DisplayName("작성자가 할일을 업데이트하고 반영된 내용을 반환할 수 있다.")
    @Test
    void update_when_author_request_then_return_updated_todo_info() {
        // given
        var todo = builderFixture.giveMeBuilder(Todo.class).set("author", "유동근").sample();

        var request = new UpdateTodoRequest(1L, "TIL 쓰기", "그냥 하지 말까?");
        given(repository.findById(any())).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn("유동근");
        // when
        TodoInfo info = service.updateTodo(request);
        // then
        then(info.title()).isEqualTo("TIL 쓰기");
        then(info.content()).isEqualTo("그냥 하지 말까?");
    }

    @DisplayName("작성자가 아니라면 할일을 수정할 수 없다.")
    @Test
    void update_when_other_request_then_throw_AccessDeniedException() {
        // given
        var todo = Todo.builder()
                .author("유동근")
                .content("잘하자")
                .title("TIL?")
                .build();

        var request = new UpdateTodoRequest(1L, "TIL 쓰기", "그냥 하지 말까?");
        given(repository.findById(any())).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn("jon");
        // when // then
        thenThrownBy(() -> service.updateTodo(request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("작성자만 삭제/수정할 수 있습니다.");
    }

    @DisplayName("존재하지 않은 할일 수정할 수 없다.")
    @Test
    void update_when_access_not_exist_todo_then_throw_NotExistException() {
        // given
        var request = new UpdateTodoRequest(1L, "TIL 쓰기", "그냥 하지 말까?");
        given(loginStatusService.getLoginCustomerName()).willReturn("jon");
        // when // then
        thenThrownBy(() -> service.updateTodo(request))
                .isInstanceOf(NotExistException.class)
                .hasMessage("존재하지 않은 할일 목록입니다.");
    }

    @DisplayName("작성자는 할일을 완료시킬 수 있다.")
    @Test
    void complete_when_author_request_then_done() {
        // given
        var todoId = 1L;
        var todo = Todo.builder()
                .author("유동근")
                .content("잘하자")
                .title("TIL?")
                .build();

        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn("유동근");
        // when
        service.complete(todoId);
        // then
        then(todo.getIsComplete()).isTrue();
    }

    @DisplayName("작성자가 아니라면 할일을 완료시킬 수 없다.")
    @Test
    void complete_when_other_request_then_done() {
        // given
        var todoId = 1L;
        var todo = Todo.builder()
                .author("유동근")
                .content("잘하자")
                .title("TIL?")
                .build();

        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn("jon");
        // when // then
        thenThrownBy(() -> service.complete(todoId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("작성자만 삭제/수정할 수 있습니다.");
        then(todo.getIsComplete()).isFalse();
    }

    @DisplayName("존재하지 않은 할일은 완료처리할 수 없다.")
    @Test
    void complete_when_not_exist_then_throw_NotExistException() {
        // given
        given(loginStatusService.getLoginCustomerName()).willReturn("jon");
        // when // then
        assertThatThrownBy(() -> service.complete(1L))
                .isInstanceOf(NotExistException.class)
                .hasMessage("존재하지 않은 할일 목록입니다.");
    }
}