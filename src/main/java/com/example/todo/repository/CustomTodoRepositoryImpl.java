package com.example.todo.repository;

import com.example.todo.dto.request.TodoSearchParam;
import com.example.todo.dto.TodoShortInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@RequiredArgsConstructor
public class CustomTodoRepositoryImpl implements CustomTodoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<TodoShortInfo> findAllByIsCompleteAndCondition(boolean isComplete, TodoSearchParam condition) {

        String query = "select ID, AUTHOR, TITLE, CONTENT, CREATED_AT " +
                "from TODO " +
                "where IS_COMPLETE = " + isComplete;

        if (condition != null && condition.title() != null) {
            query += " and title like '%" + condition.title() + "%'";
        }

        return jdbcTemplate.query(query, (rs, rowNum) -> new TodoShortInfo(
                rs.getLong("ID"),
                rs.getString("AUTHOR"),
                rs.getString("TITLE"),
                rs.getString("CONTENT"),
                rs.getTimestamp("CREATED_AT").toLocalDateTime()
        ));
    }
}
