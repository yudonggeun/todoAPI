package com.example.todo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Boolean isComplete;

    @Builder
    private Todo(String author, String title, String content, Boolean isComplete) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.isComplete = isComplete;
    }
}
