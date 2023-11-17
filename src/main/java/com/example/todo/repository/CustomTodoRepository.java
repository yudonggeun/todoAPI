package com.example.todo.repository;

import com.example.todo.dto.request.TodoSearchParam;
import com.example.todo.dto.TodoShortInfo;

import java.util.List;

public interface CustomTodoRepository {

    List<TodoShortInfo> findAllByIsCompleteAndCondition(boolean isComplete, TodoSearchParam condition);
}
