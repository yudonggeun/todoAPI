package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Comment;
import com.example.todo.domain.Todo;
import com.example.todo.dto.CommentInfo;
import com.example.todo.dto.request.CreateCommentRequest;
import com.example.todo.dto.request.UpdateCommentRequest;
import com.example.todo.repository.CommentRepository;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final LoginStatusService loginStatus;

    public CommentInfo createComment(CreateCommentRequest request) {

        if (!todoRepository.existsById(request.todoId())) {
            throw new NotExistException();
        }

        Comment comment = commentRepository.save(Comment.builder()
                .author(loginStatus.getLoginCustomerName())
                .content(request.content())
                .todo(Todo.foreignKey(request.todoId()))
                .build());

        return CommentInfo.of(comment);
    }

    public CommentInfo updateComment(UpdateCommentRequest request) {
        var comment = commentRepository.findById(request.id())
                .orElseThrow(() -> new NotExistException("댓글이 존재하지 않습니다."));

        checkLoginCustomerEqualAuthorOfComment(comment);
        comment.update(request);
        return CommentInfo.of(comment);
    }

    public void deleteComment(Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(NotExistException::new);

        checkLoginCustomerEqualAuthorOfComment(comment);
        commentRepository.delete(comment);
    }

    // Comment를 넘기는 것보다 String author를 넘겨서 의존성을 낮추는 것이 좋을까요?
    private void checkLoginCustomerEqualAuthorOfComment(Comment comment) {
        if (!comment.getAuthor().equals(loginStatus.getLoginCustomerName())) {
            throw new AccessDeniedException("작성자만 삭제/수정할 수 있습니다.");
        }
    }
}
