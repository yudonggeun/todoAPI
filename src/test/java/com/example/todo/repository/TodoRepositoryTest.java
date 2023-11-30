package com.example.todo.repository;

import com.example.todo.RepositoryTest;
import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoShortInfo;
import com.example.todo.dto.request.TodoSearchParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class TodoRepositoryTest extends RepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @DisplayName("findAllByIsCompleteAndCondition(false, null)일 때 조회된 할 일 목록 중에 완료된 것은 없다.")
    @Test
    void test() {
        // given
        for (int i = 0; i < 10; i++)
            saveSampleTodo("title" + i, "author", "content" + i, i % 2 == 0);
        // when
        var result = todoRepository.findAllByIsCompleteAndCondition(false, null);
        // then
        for (var todoInfo : result) {
            System.out.println(todoInfo.title());
        }
        assertThat(result).hasSize(5)
                .map(TodoShortInfo::title)
                .contains("title1", "title3", "title5", "title7", "title9");
    }

    void saveSampleTodo(String title, String author, String content, boolean isComplete) {
        Todo todo = Todo.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();

        if (isComplete) todo.complete();
        todoRepository.saveAndFlush(todo);
    }

    @DisplayName("특정 제목이 포함된 todo를 조회한다.")
    @Test
    void findAllByIsCompleteAndCondition() {
        // given
        for (int i = 0; i < 10; i++)
            saveSampleTodo("title" + i, "author", "content" + i, i % 2 == 0);
        var condition = new TodoSearchParam("title1");
        // when
        var result = todoRepository.findAllByIsCompleteAndCondition(false, condition);
        // then
        assertThat(result).hasSize(1)
                .map(TodoShortInfo::title)
                .contains("title1");
    }
}