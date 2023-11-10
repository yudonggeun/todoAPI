package com.example.todo.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @PostMapping
    public ResponseEntity<?> createComment(){
        return null;
    }

    @PatchMapping
    public ResponseEntity<?> updateComment(){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(){
        return null;
    }
}
