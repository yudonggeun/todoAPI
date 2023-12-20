package com.example.todo.repository;

import static com.example.todo.domain.QTodo.todo;
import static org.springframework.util.StringUtils.hasText;

import com.example.todo.dto.TodoShortInfo;
import com.example.todo.dto.request.TodoSearchParam;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class CustomTodoRepositoryImpl implements CustomTodoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;
    private JPAQueryFactory factory;

    @PostConstruct
    void init(){
        factory = new JPAQueryFactory(em);
    }

    @Override
    public List<TodoShortInfo> findAllByIsCompleteAndCondition(boolean isComplete,
        TodoSearchParam condition) {

//        String query = "select ID, AUTHOR, TITLE, CONTENT, CREATED_AT " +
//            "from TODO " +
//            "where IS_COMPLETE = " + isComplete;
//
//        if (condition != null && condition.title() != null) {
//            query += " and title like '%" + condition.title() + "%'";
//        }
//        // (queryDsl를 사용하지 않고 구현할 때) jdbc template은 잘 사용하지 않아서 더 좋은 구현에 대해서 궁금합니다.
//        return jdbcTemplate.query(query, (rs, rowNum) -> new TodoShortInfo(
//            rs.getLong("ID"),
//            rs.getString("AUTHOR"),
//            rs.getString("TITLE"),
//            rs.getString("CONTENT"),
//            rs.getTimestamp("created_at").toLocalDateTime()
//        ));
        if(condition == null) {
            condition = new TodoSearchParam(null);
        }

        List<TodoShortInfo> result = factory.select(Projections.constructor(TodoShortInfo.class,
                todo.id,
                todo.author,
                todo.title,
                todo.content,
                todo.createdAt
            ))
            .from(todo)
            .where(
                todo.isComplete.eq(isComplete),
                titleEq(condition.title())
                )
            .fetch();

        return result;
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? todo.title.contains(title) : null;
    }
}
