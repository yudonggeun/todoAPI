package com.example.todo.dto.request;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("할일 생성 요청 DTO 테스트")
class CreateTodoRequestTest {

    @DisplayName("할일 생성 요청 정보를 담은 entity를 생성한다.")
    @Test
    void when_createTodoRequest_convert_entity_then_match_info() {
        // given
        var title = "강의 듣기";
        var content = "해야할일 1";
        var author = "홍길동";
        var dto = new CreateTodoRequest(title, content);
        // when
        Todo todo = dto.toEntity(author);
        // then
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getContent()).isEqualTo(content);
        assertThat(todo.getAuthor()).isEqualTo(author);
    }
}