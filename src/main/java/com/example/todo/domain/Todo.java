package com.example.todo.domain;

import com.example.todo.dto.request.UpdateTodoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Todo extends BaseTimeEntity {

    @Column
    private String author;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private Boolean isComplete = false;
    @OneToMany(mappedBy = "todo")
    private List<Comment> comments = new ArrayList<>();

    private Todo(Long id){
        super(id);
    }

    // 댓글 생성시 할일의 id를 매핑할 때 사용하려는 mock 스러운 객체 생성 메서드인데..
    // 이런 방식이 불필요한 구현인가요?
    public static Todo foreignKey(Long id){
        return new Todo(id);
    }
    @Builder
    private Todo(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public void update(UpdateTodoRequest request) {
        if(request.title() != null) title = request.title();
        if(request.content() != null) content = request.content();
    }

    public void complete() {
        isComplete = true;
    }
}