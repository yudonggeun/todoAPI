package com.example.todo.service;

import com.example.todo.domain.Comment;
import com.example.todo.domain.Todo;
import com.example.todo.dto.CommentInfo;
import com.example.todo.dto.CreateCommentRequest;
import com.example.todo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private String getLoginCustomerName() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
