package com.example.todo.service;

import com.example.todo.domain.Comment;
import com.example.todo.domain.Todo;
import com.example.todo.dto.CommentInfo;
import com.example.todo.dto.request.CreateCommentRequest;
import com.example.todo.dto.request.UpdateCommentRequest;
import com.example.todo.repository.CommentRepository;
import com.example.todo.repository.TodoRepository;
import com.example.todo.security.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("댓글 서비스 테스트")
class CommentServiceTest {

    TodoRepository todoRepository = mock();
    CommentRepository commentRepository = mock();
    LoginStatusService loginStatusService = mock();
    CommentService commentService = new CommentService(commentRepository, todoRepository, loginStatusService);

    @BeforeEach
    void initSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = User.builder()
                .username("sample user")
                .password("test")
                .build();
        context.setAuthentication(new JwtToken(user, List.of()));
    }

    @DisplayName("댓글 생성 성공시 생성된 댓글 정보는 요청과 일치해야한다.")
    @Test
    void when_create_request_process_successfully_then_return_comment_info() {
        // given
        var request = new CreateCommentRequest(1L, "아주 잘했어요!");
        when(todoRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.save(any())).thenReturn(Comment.builder()
                .author("sample user")
                .content(request.content())
                .todo(Todo.foreignKey(request.todoId()))
                .build()
        );
        // when
        CommentInfo result = commentService.createComment(request);
        // then
        assertThat(result.todoId()).isEqualTo(1L);
        assertThat(result.content()).isEqualTo("아주 잘했어요!");
        assertThat(result.author()).isEqualTo("sample user");
    }

    @DisplayName("없는 할일에 댓글을 작성하면 NotExistException 이 발생한다.")
    @Test
    void when_todo_is_not_exist_then_throw_exception() {
        // given
        var request = new CreateCommentRequest(1L, "아주 잘했어요!");
        when(todoRepository.existsById(any())).thenReturn(false);
        // when // then
        assertThatThrownBy(() -> commentService.createComment(request));
    }

    @DisplayName("댓글 수정시 로그인하지 않으면 에외가 발생한다.")
    @Test
    void when_not_login_then_throw_exception() {
        // given
        var request = new UpdateCommentRequest(1L, "change content");
        when(loginStatusService.getLoginCustomerName()).thenThrow(IllegalArgumentException.class);
        // when // then
        assertThatThrownBy(() -> commentService.updateComment(request));
    }

    @DisplayName("댓글 수정시 작성자가 아니라면 예외가 발생한다.")
    @Test
    void when_loginUser_is_not_author_then_match_changed_content() {
        // given
        var request = new UpdateCommentRequest(1L, "change content");
        when(loginStatusService.getLoginCustomerName()).thenReturn("test1234");
        when(commentRepository.findById(request.id()))
                .thenReturn(Optional.of(Comment.builder()
                        .todo(Todo.foreignKey(1L))
                        .content("default")
                        .author("other")
                        .build()));
        // when // then
        assertThatThrownBy(() -> commentService.updateComment(request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("작성자만 삭제/수정할 수 있습니다.");
    }
    @DisplayName("댓글 수정시 수정 내용이 반영된다.")
    @Test
    void when_update_comment_then_match_changed_content() {
        // given
        var request = new UpdateCommentRequest(1L, "change content");
        when(loginStatusService.getLoginCustomerName()).thenReturn("test1234");
        when(commentRepository.findById(request.id()))
                .thenReturn(Optional.of(Comment.builder()
                        .todo(Todo.foreignKey(1L))
                        .content("default")
                        .author("test1234")
                        .build()));
        // when
        CommentInfo changedInfo = commentService.updateComment(request);
        // then
        assertThat(changedInfo.content()).isEqualTo(request.content());
        assertThat(changedInfo.author()).isEqualTo("test1234");
    }
}