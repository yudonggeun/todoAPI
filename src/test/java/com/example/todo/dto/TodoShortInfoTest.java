package com.example.todo.dto;

import com.example.todo.TestSupport;
import com.example.todo.domain.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@DisplayName("간단 할일 정보 DTO 테스트")
class TodoShortInfoTest extends TestSupport {

    @DisplayName("할일 엔티디의 정보와 간단 할일 정보 DTO의 정보와 일치해야한다.")
    @Test
    void when_convert_dto_then_match_entity_info_to_dto() {
        // given
        Todo todo = spy(builderFixture.giveMeOne(Todo.class));
        given(todo.getId()).willReturn(1L);
        given(todo.getCreatedAt()).willReturn(LocalDateTime.now());
        // when
        TodoShortInfo dto = TodoShortInfo.of(todo);
        // then
        then(dto.author()).isEqualTo(todo.getAuthor());
        then(dto.content()).isEqualTo(todo.getContent());
        then(dto.title()).isEqualTo(todo.getTitle());
        then(dto.createdAt()).isEqualTo(todo.getCreatedAt());
        then(dto.id()).isEqualTo(todo.getId());
    }
}