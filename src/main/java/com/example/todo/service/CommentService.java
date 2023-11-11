package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.domain.Comment;
import com.example.todo.domain.Todo;
import com.example.todo.dto.CommentInfo;
import com.example.todo.dto.request.CreateCommentRequest;
import com.example.todo.dto.request.UpdateCommentRequest;
import com.example.todo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentInfo createComment(CreateCommentRequest request) {

        Comment comment = commentRepository.save(Comment.builder()
                .author(getLoginCustomerName())
                .content(request.content())
                .todo(Todo.foreignKey(request.todoId()))
                .build());

        return CommentInfo.of(comment);
    }

    public CommentInfo updateComment(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.id())
                .orElseThrow(() -> new NotExistException("댓글이 존재하지 않습니다."));

        if (!comment.getAuthor().equals(getLoginCustomerName())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        comment.update(request);
        return CommentInfo.of(comment);
    }

    public void deleteComment(Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(NotExistException::new);

        if(!comment.getAuthor().equals(getLoginCustomerName())){
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
    }

    private String getLoginCustomerName() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
