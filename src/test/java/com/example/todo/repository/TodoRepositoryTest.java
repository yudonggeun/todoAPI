package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @DisplayName("findAllByIsComplete(false)일 때 조회된 할 일 목록 중에 완료된 것은 없다.")
    @Test
    void test() {
        // given
        for (int i = 0; i < 10; i++)
            saveSampleTodo("title" + i, "author", "content" + i, i % 2 == 0);
        // when
        List<TodoInfo> result = todoRepository.findAllByIsComplete(false);
        // then
        for (TodoInfo todoInfo : result) {
            System.out.println(todoInfo.title());
        }
        assertThat(result).hasSize(5)
                .map(TodoInfo::title)
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
}