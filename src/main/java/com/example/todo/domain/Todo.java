package com.example.todo.domain;

import com.example.todo.dto.request.UpdateTodoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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