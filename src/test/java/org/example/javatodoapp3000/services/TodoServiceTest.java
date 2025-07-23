package org.example.javatodoapp3000.services;

import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.exceptions.NotFoundException;
import org.example.javatodoapp3000.models.Todo;
import org.example.javatodoapp3000.models.TodoState;
import org.example.javatodoapp3000.repository.TodoRepo;
import org.example.javatodoapp3000.utils.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.DirtiesContext;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoServiceTest {

    @Test
    void findAllTodos_returns_all_todos_as_todo_dto() {
        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        IdService mockIdService = Mockito.mock(IdService.class);
        TodoService todoService = new TodoService(mockTodoRepo, mockIdService);
        when(mockIdService.generateId()).thenReturn("1234");

        TodoState initialState = new TodoState("Initial STate", Status.OPEN);
        TodoState updateState = new TodoState("Update State", Status.IN_PROGRESS);
        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, initialState);
        timeline.put(2, updateState);
        Todo todo = new Todo("1234", timeline);
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);

        when(mockTodoRepo.findAll()).thenReturn(todos);

        TodoDto todoDto = new TodoDto("1234","Update State", Status.IN_PROGRESS);
        List<TodoDto> expected = new ArrayList<>();
        expected.add(todoDto);

        //When
        List<TodoDto> result = todoService.findAllTodos();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void addTodo() {
        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        IdService mockIdService = Mockito.mock(IdService.class);

        TodoService todoService = new TodoService(mockTodoRepo, mockIdService);
        when(mockIdService.generateId()).thenReturn("1234");

        TodoDto input = new TodoDto("1234","Hallo", Status.OPEN);

        Todo todo = new Todo("1234", "Hallo", Status.OPEN);

        when(mockTodoRepo.save(any(Todo.class))).thenReturn(todo);

        // When
        TodoDto result =  todoService.addTodo(input);

        // Then
        assertEquals(input,result);

    }

    @Test
    void findTodoById() throws NotFoundException {
        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        IdService mockIdService = Mockito.mock(IdService.class);
        TodoService todoService = new TodoService(mockTodoRepo, mockIdService);

        TodoDto expected = new TodoDto("1234", "Updated Content", Status.IN_PROGRESS);
        when(mockIdService.generateId()).thenReturn("1234");


        Todo todo = new Todo("1234", "Updated Content", Status.IN_PROGRESS);

        when(mockTodoRepo.findTodoById(any())).thenReturn(todo);

        // WHEN
        TodoDto actual = todoService.findTodoById("1234");

        //Then
        assertEquals(expected,actual);
    }

    @Test
    void updateTodo() throws NotFoundException {
        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        IdService mockIdService = Mockito.mock(IdService.class);
        TodoService todoService = new TodoService(mockTodoRepo, mockIdService);
        when(mockIdService.generateId()).thenReturn("1234");

        Todo original = new Todo("1234", "Initial Content", Status.OPEN);

        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, new  TodoState("Initial Content", Status.OPEN));
        timeline.put(2, new TodoState("Initial Content", Status.IN_PROGRESS));
        Todo updatedTodo = new Todo ("1234", timeline);

        TodoDto expected = new TodoDto("1234", "Initial Content", Status.IN_PROGRESS);

        when(mockTodoRepo.findTodoById(any())).thenReturn(original);

        when(mockTodoRepo.save(any())).thenReturn(updatedTodo);
        // When
        TodoDto actual = todoService.updateTodo(expected);

        // Then
        assertEquals(expected,actual);
    }

    @Test
    void setTodoToDeleted() {
        TodoRepo mockTodoRepo = Mockito.mock(TodoRepo.class);
        IdService mockIdService = Mockito.mock(IdService.class);
        TodoService todoService = new TodoService(mockTodoRepo, mockIdService);
        when(mockIdService.generateId()).thenReturn("1234");

        Todo original = new Todo("1234", "Initial Content", Status.OPEN);

        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, new  TodoState("Initial Content", Status.OPEN));
        timeline.put(2, new TodoState("Initial Content", Status.DELETED));
        Todo deletedTodo = new Todo ("1234", timeline);

        when(mockTodoRepo.findTodoById(any())).thenReturn(original);
        when(mockTodoRepo.save(any())).thenReturn(deletedTodo);

        // When
        try {
            todoService.setTodoToDeleted("1234");
        } catch (Exception e) {
            fail();
        }
    }
}