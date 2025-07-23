package org.example.javatodoapp3000.services;

import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.models.Todo;
import org.example.javatodoapp3000.models.TodoState;
import org.example.javatodoapp3000.repository.TodoRepo;
import org.example.javatodoapp3000.utils.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoServiceTest {
    static UUID uuid =  UUID.randomUUID();

    @Test
    void findAllTodos_returns_all_todos_as_todo_dto() {
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        TodoState initialState = new TodoState("Initial STate", Status.OPEN);
        TodoState updateState = new TodoState("Update State", Status.CLOSED);
        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, initialState);
        timeline.put(2, updateState);
        Todo todo = new Todo(uuid.toString(), timeline);
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);

        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        TodoService todoService = new TodoService(mockTodoRepo);

        when(mockTodoRepo.findAll()).thenReturn(todos);

        TodoDto todoDto = new TodoDto(uuid.toString(),"Update State", Status.CLOSED);
        List<TodoDto> expected = new ArrayList<>();
        expected.add(todoDto);

        //When
        List<TodoDto> result = todoService.findAllTodos();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void addTodo() {
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        TodoService todoService = new TodoService(mockTodoRepo);

        TodoDto input = new TodoDto(uuid.toString(),"Hallo", Status.OPEN);

        Todo todo = new Todo(uuid.toString(), "Hallo", Status.OPEN);

        when(mockTodoRepo.save(any(Todo.class))).thenReturn(todo);

        // When
        TodoDto result =  todoService.addTodo(input);

        // Then
        assertEquals(input,result);

    }
}