package com.example.todo.controller;

import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.request.CreateTodoRequest;
import com.example.todo.dto.request.TodoSearchParam;
import com.example.todo.dto.request.UpdateTodoRequest;
import com.example.todo.dto.response.MessageResponse;
import com.example.todo.dto.response.TodoInfoListResponse;
import com.example.todo.service.TodoService;
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

@Tag(name = "Todo", description = "할일 API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "할일 생성", description = "할일 보드를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할일 생성 성공",
                    content = @Content(schema = @Schema(implementation = TodoInfo.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody CreateTodoRequest request) {
        return ResponseEntity.ok(todoService.createTodo(request));
    }

    @Operation(summary = "할일 상세 조회", description = "자신이 만든 할일을 상세히 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할일 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = TodoInfo.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTodo(
            @Parameter(description = "할일 식별자", example = "1")
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(todoService.getTodoInfo(id));
    }

    @Operation(summary = "할일 목록 조회", description = "동료와 나의 할일 목록을 조회할 수 있습니다. 다른 사람의 할일 목록 정보는 필터링 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할일 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TodoInfoListResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getTodoList(@ModelAttribute TodoSearchParam condition) {
        return ResponseEntity.ok(todoService.getTodoInfoList(condition));
    }

    @Operation(summary = "할일 수정", description = "자신의 할일을 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할일 수정 성공",
                    content = @Content(schema = @Schema(implementation = TodoInfo.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<?> updateTodo(@Valid @RequestBody UpdateTodoRequest request) {
        TodoInfo info = todoService.updateTodo(request);
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "할일 완료", description = "할일을 완료 처리할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할일 완료 처리 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> completeTodo(
            @Parameter(description = "할일 식별자", example = "1")
            @PathVariable("id") Long id
    ) {
        todoService.complete(id);
        return ResponseEntity.ok(new MessageResponse("success", "할일 완료"));
    }

    @Operation(summary = "할일 삭제", description = "할일을 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할일 삭제 처리 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(
            @Parameter(description = "할일 식별자", example = "1")
            @PathVariable("id") Long id
    ) {
        todoService.delete(id);
        return ResponseEntity.ok(new MessageResponse("success", "할일 삭제 완료"));
    }
}
