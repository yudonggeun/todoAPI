package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("간단 할일 정보 DTO 테스트")
class TodoShortInfoTest {

    @DisplayName("할일 엔티디의 정보와 간단 할일 정보 DTO의 정보와 일치해야한다.")
    @Test
    void when_convert_dto_then_match_entity_info_to_dto() {
        // given
        var todo = Mockito.spy(Todo.builder()
                .title("하지마")
                .content("장난")
                .author("누구야")
                .build());

        given(todo.getId()).willReturn(1L);
        given(todo.getCreatedAt()).willReturn(LocalDateTime.now());
        // when
        TodoShortInfo dto = TodoShortInfo.of(todo);
        // then
        assertThat(dto.author()).isEqualTo(todo.getAuthor());
        assertThat(dto.content()).isEqualTo(todo.getContent());
        assertThat(dto.title()).isEqualTo(todo.getTitle());
        assertThat(dto.createdAt()).isEqualTo(todo.getCreatedAt());
        assertThat(dto.id()).isEqualTo(todo.getId());
    }
}