package com.example.todo.controller;

import com.example.todo.dto.CommentInfo;
import com.example.todo.dto.request.CreateCommentRequest;
import com.example.todo.dto.request.UpdateCommentRequest;
import com.example.todo.dto.response.MessageResponse;
import com.example.todo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "할일 보드에 댓글을 작성할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공",
                    content = @Content(schema = @Schema(implementation = CommentInfo.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest request) {
        CommentInfo info = commentService.createComment(request);
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "댓글 수정", description = "자신의 댓글을 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(schema = @Schema(implementation = CommentInfo.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<?> updateComment(@Valid @RequestBody UpdateCommentRequest request) {
        CommentInfo info = commentService.updateComment(request);
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "댓글 삭제", description = "자신의 댓글을 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(
            @Parameter(description = "댓글 식별자", example = "1")
            @PathVariable("id") Long id
    ) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(new MessageResponse("success", "삭제 성공하였습니다."));
    }
}
