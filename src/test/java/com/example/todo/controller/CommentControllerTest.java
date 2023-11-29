package com.example.todo.controller;

import com.example.todo.IntegrationTest;
import com.example.todo.domain.Comment;
import com.example.todo.domain.Todo;
import com.example.todo.dto.request.CreateCommentRequest;
import com.example.todo.dto.request.UpdateCommentRequest;
import com.example.todo.repository.CommentRepository;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends IntegrationTest {

    @Autowired
    TodoRepository todoRepository;
    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    void clear() {
        commentRepository.deleteAll();
        todoRepository.deleteAll();
    }

    @DisplayName("댓글 생성 성공")
    @Test
    void createComment() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        var request = new CreateCommentRequest(todo.getId(), "공부안해요?");
        // when // then
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(user("tiger"))
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.author").value("tiger"),
                        jsonPath("$.content").value("공부안해요?"),
                        jsonPath("$.todoId").value(todo.getId()),
                        jsonPath("$.id").exists()
                );
    }

    @DisplayName("댓글 생성 실패 : 인증이 되지 않은 경우")
    @Test
    void createCommentFailWhenNotAuthorize() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        var request = new CreateCommentRequest(todo.getId(), "공부안해요?");
        // when // then
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpectAll(expectedWhenTokenIsNotExists());
    }

    @DisplayName("댓글 수정 성공")
    @Test
    void updateComment() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        Comment comment = saveComment(todo, "메롱", "user1");
        var request = new UpdateCommentRequest(comment.getId(), "공부안해요?");
        // when // then
        mockMvc.perform(patch("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(user("user1"))
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.author").value("user1"),
                        jsonPath("$.content").value("공부안해요?"),
                        jsonPath("$.todoId").value(todo.getId()),
                        jsonPath("$.id").exists()
                );
    }

    @DisplayName("댓글 수정 실패 : 인증이 되지 않은 경우")
    @Test
    void updateCommentFailWhenNotAuthorized() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        Comment comment = saveComment(todo, "메롱", "user1");
        var request = new UpdateCommentRequest(comment.getId(), "공부안해요?");
        // when // then
        mockMvc.perform(patch("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpectAll(expectedWhenTokenIsNotExists());
    }

    @DisplayName("댓글 수정 실패 : 작성자가 요청하지 않은 경우")
    @Test
    void updateCommentFailWhenNotAuthor() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        Comment comment = saveComment(todo, "메롱", "user1");
        var request = new UpdateCommentRequest(comment.getId(), "공부안해요?");
        // when // then
        mockMvc.perform(patch("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(user("user2"))
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.status").value("unauthorized"),
                        jsonPath("$.message").value("작성자만 삭제/수정할 수 있습니다.")
                );
    }

    @DisplayName("댓글 삭제 성공")
    @Test
    void deleteComment() throws Exception {
        // given
        Todo todo = saveTodo("todoauthor", "놀기", "신나게 놀아볼까?");
        Comment comment = saveComment(todo, "메롱", "user1");
        // when // then
        mockMvc.perform(delete("/comment/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user1"))
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.message").value("삭제 성공하였습니다.")
                );
    }

    @DisplayName("댓글 삭제 실패 : 인증이 되지 않은 경우")
    @Test
    void deleteCommentFailWhenNotAuthorized() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        Comment comment = saveComment(todo, "메롱", "user1");
        // when // then
        mockMvc.perform(delete("/comment/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpectAll(expectedWhenTokenIsNotExists());
    }

    @DisplayName("댓글 삭제 실패 : 작성자가 요청하지 않은 경우")
    @Test
    void deleteCommentFailWhenNotAuthor() throws Exception {
        // given
        Todo todo = saveTodo("testuser", "놀기", "신나게 놀아볼까?");
        Comment comment = saveComment(todo, "메롱", "user1");
        // when // then
        mockMvc.perform(delete("/comment/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user2"))
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.status").value("unauthorized"),
                        jsonPath("$.message").value("작성자만 삭제/수정할 수 있습니다.")
                );

    }

    private Comment saveComment(Todo todo, String conent, String author) {
        return commentRepository.save(Comment.builder()
                .todo(todo)
                .content(conent)
                .author(author)
                .build());
    }

    private Todo saveTodo(String author, String title, String content) {
        return todoRepository.saveAndFlush(Todo.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());
    }

    ResultMatcher[] expectedWhenTokenIsNotExists() {
        return new ResultMatcher[]{
                status().isBadRequest(),
                jsonPath("$.status").value("bad request"),
                jsonPath("$.message").value("토큰이 유효하지 않습니다.")
        };
    }
}