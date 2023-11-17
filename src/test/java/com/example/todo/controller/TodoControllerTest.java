package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.repository.CommentRepository;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest extends ControllerTest {

    @DisplayName("인증하지 않은 경우")
    @Nested
    class NoAuthenticatedCases {

        @DisplayName("할일 생성 실패")
        @Test
        void createTodoFailWhenNoUser() throws Exception {
            // given
            var title = "스프링 정복하기";
            var content = "안할거지롱";
            var request = new CreateTodoRequest(title, content);
            // when // then
            mockMvc.perform(post("/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpectAll(expectedWhenTokenIsNotExists());
        }

        @DisplayName("할일 상세 조회 실패")
        @Test
        void getTodoFailWhenNoUser() throws Exception {
            // given
            var id = "1";
            // when // then
            mockMvc.perform(get("/todo/" + id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(expectedWhenTokenIsNotExists());
        }

        @DisplayName("할일 목록 조회 실패")
        @Test
        void getListTodoFailWhenNoUser() throws Exception {
            // given
            var title = "안녕";
            // when // then
            mockMvc.perform(get("/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("title", title))
                    .andExpectAll(expectedWhenTokenIsNotExists());
        }

        @DisplayName("할일 수정 실패")
        @Test
        void updateTodoFailWhenNoUser() throws Exception {
            // given
            var request = new UpdateTodoRequest(1L, "메롱", "죄송합니다.");
            // when // then
            mockMvc.perform(patch("/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpectAll(expectedWhenTokenIsNotExists());
        }

        @DisplayName("할일 완료 처리 실패")
        @Test
        void completeTodoFailWhenNoUser() throws Exception {
            // when // then
            mockMvc.perform(patch("/todo" + 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(expectedWhenTokenIsNotExists());
        }

        @DisplayName("할일 삭제 처리 실패")
        @Test
        void deleteTodoFailWhenNoUser() throws Exception {
            // when // then
            mockMvc.perform(delete("/todo" + 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(expectedWhenTokenIsNotExists());
        }

        ResultMatcher[] expectedWhenTokenIsNotExists() {
            return new ResultMatcher[]{status().isBadRequest(),
                    jsonPath("$.status").value("bad request"),
                    jsonPath("$.message").value("토큰이 유효하지 않습니다.")};
        }

    }

    @Autowired
    TodoRepository todoRepository;
    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    void clear() {
        commentRepository.deleteAll();
        todoRepository.deleteAll();
    }

    @DisplayName("할일 생성 성공")
    @Test
    void createTodo() throws Exception {
        // given
        var title = "스프링 정복하기";
        var content = "안할거지롱";
        var request = new CreateTodoRequest(title, content);
        // when // then
        mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .with(user("testuser"))
        ).andDo(print()
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id").exists(),
                jsonPath("$.author").value("testuser"),
                jsonPath("$.title").value(title),
                jsonPath("$.content").value(content),
                jsonPath("$.createdAt").exists(),
                jsonPath("$.content").exists()
        );
    }

    @DisplayName("할일 상세 조회 성공")
    @Test
    void getTodo() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "test", "test content");
        // when // then
        mockMvc.perform(get("/todo/" + todo.getId())
                        .with(user("testuser")))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.author").value("testuser"),
                        jsonPath("$.title").value(todo.getTitle()),
                        jsonPath("$.content").value(todo.getContent()),
                        jsonPath("$.createdAt").exists(),
                        jsonPath("$.content").exists()
                );
    }

    @DisplayName("할일 상세 조회 실패")
    @Test
    void getTodoWhenIsNotAuthor() throws Exception {
        // given
        Todo todo = saveTodo("hacker", "test", "test content");
        // when // then
        mockMvc.perform(get("/todo/" + todo.getId())
                        .with(user("testuser")))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.status").value("unauthorized"),
                        jsonPath("$.message").value("작성자만 조회할 수 있습니다.")
                );
    }

    @DisplayName("할일 목록 조회 성공")
    @Test
    void getList() throws Exception {
        // given
        var titleCondition = "스프링";
        saveTodo("testuser", "스프링 시큐리티", "test1");
        saveTodo("testuser", "다시보는 스프링", "test2");
        saveTodo("otheruser", "다시보는 스프링", "test2");
        saveTodo("testuser", "다시보는 자바", "test3");
        // when // then
        mockMvc.perform(get("/todo")
                        .param("title", titleCondition)
                        .with(user("testuser"))
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size").value(2),

                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[0].id").exists(),
                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[0].title").value("다시보는 스프링"),
                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[0].content").value("test2"),
                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[0].createdAt").exists(),

                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[1].id").exists(),
                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[1].title").value("스프링 시큐리티"),
                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[1].content").value("test1"),
                        jsonPath("$.data[?(@.author == \"testuser\")].todoList[1].createdAt").exists(),

                        jsonPath("$.data[?(@.author == \"otheruser\")].todoList[0].id").exists(),
                        jsonPath("$.data[?(@.author == \"otheruser\")].todoList[0].title").value("다시보는 스프링"),
                        jsonPath("$.data[?(@.author == \"otheruser\")].todoList[0].content").isEmpty(),
                        jsonPath("$.data[?(@.author == \"otheruser\")].todoList[0].createdAt").exists()
                );
    }

    private Todo saveTodo(String author, String title, String content) {
        return todoRepository.saveAndFlush(Todo.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());
    }
}