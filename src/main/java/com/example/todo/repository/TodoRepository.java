package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, CustomTodoRepository {
//    @Query("select new com.example.todo.dto.TodoInfo(t.id, t.author, t.title, t.content, t.createdAt) " +
//            "from Todo t " +
//            "where t.isComplete = :isComplete")
//    List<TodoInfo> findAllByIsComplete(@Param("isComplete") boolean isCompleted);
    @EntityGraph(attributePaths = {"comments"})
    Optional<Todo> findFetchById(Long id);
}
