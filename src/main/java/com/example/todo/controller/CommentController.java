package com.example.todo.controller;


import com.example.todo.dto.CommentInfo;
import com.example.todo.dto.CreateCommentRequest;
import com.example.todo.dto.MessageResponse;
import com.example.todo.dto.UpdateCommentRequest;
import com.example.todo.service.CommentService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(CreateCommentRequest request) {
        CommentInfo info = commentService.createComment(request);
        return ResponseEntity.ok(info);
    }

    @PatchMapping
    public ResponseEntity<?> updateComment(UpdateCommentRequest request) {
        CommentInfo info = commentService.updateComment(request);
        return ResponseEntity.ok(info);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathParam("id") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(new MessageResponse("success", "삭제 성공하였습니다."));
    }
}
