package com.example.todo.domain;

import com.example.todo.dto.request.UpdateCommentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTimeEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    private Todo todo;
    @Column
    private String author;
    @Column
    private String content;

    @Builder
    private Comment(Todo todo, String author, String content) {
        this.todo = todo;
        this.author = author;
        this.content = content;
    }

    public void update(UpdateCommentRequest request) {
        if(request.content() != null) this.content = request.content();
    }
}
