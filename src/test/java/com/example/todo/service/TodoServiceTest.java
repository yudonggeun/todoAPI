package com.example.todo.service;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoInfo;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("할일 서비스 테스트")
class TodoServiceTest {

    TodoRepository repository = mock();
    CommentRepository commentRepository = mock();
    LoginStatusService loginStatusService = mock();
    TodoService service = new TodoService(repository, commentRepository, loginStatusService);

    @DisplayName("작성자가 아니라면 할일을 조회할 수 없다.")
    @Test
    void when_other_user_get_todo_then_throw_exception() {
        // given
        var id = 1L;
        var todo = Todo.builder()
                .author("test")
                .content("content")
                .build();
        given(repository.findFetchById(1L)).willReturn(Optional.of(todo));
        given(loginStatusService.getLoginCustomerName()).willReturn("other user");
        // when // then
        assertThatThrownBy(() -> service.getTodoInfo(id))
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
        assertThat(info.author()).isEqualTo(author);
        assertThat(info.content()).isEqualTo(content);
        assertThat(info.title()).isEqualTo(title);
    }

    private String randomString(){
        return UUID.randomUUID().toString();
    }
}