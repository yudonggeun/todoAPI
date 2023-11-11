package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("select new com.example.todo.dto.TodoInfo(t.id, t.author, t.title, t.content, t.createdAt) " +
            "from Todo t " +
            "where t.isComplete = :isComplete")
    List<TodoInfo> findAllByIsComplete(@Param("isComplete") boolean isCompleted);
}
