package com.example.todo.repository;

import com.example.todo.dto.TodoInfo;
import com.example.todo.dto.TodoSearchParam;

import java.util.List;

public interface CustomTodoRepository {

    List<TodoInfo> findAllByIsCompleteAndCondition(boolean isComplete, TodoSearchParam condition);
}
