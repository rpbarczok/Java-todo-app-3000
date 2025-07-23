package org.example.javatodoapp3000.models;

import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.utils.Status;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class TodoTest {
    static UUID uuid =  UUID.randomUUID();

    @Test
    void getLatestStateAsTodoDto_returns_latest_state_of_todo() {
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        //GIVEN

        TodoState initialState = new TodoState("Initial State", Status.OPEN);
        TodoState updateState = new TodoState("Update State", Status.IN_PROGRESS);
        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, initialState);
        timeline.put(2, updateState);
        Todo todo = new Todo(uuid.toString(), timeline);

        TodoDto expected = new TodoDto(uuid.toString(), "Update State", Status.IN_PROGRESS);

        // When
        TodoDto actual = todo.getLatestStateAsTodoDto();

        // THEN
        assertEquals(expected, actual);

    }

    @Test
    void updateTodo_returns_updated_todo() {
        // Given
        TodoState initialState = new TodoState("Initial State", Status.OPEN);
        TodoState updateState = new TodoState("Update State", Status.IN_PROGRESS);
        TodoDto newTodoDto = new TodoDto(uuid.toString(), "Update State", Status.IN_PROGRESS);
        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, initialState);
        Todo original = new Todo(uuid.toString(), timeline);

        Todo expected = original;
        original.getTodoTimeline().put(2, updateState);

        // When

        Todo actual = original.updateTodo(newTodoDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void setStateDeleted_returns_deleted_todo() {
        // Given
        TodoState initialState = new TodoState("Initial State", Status.OPEN);
        TodoState updateState = new TodoState("Initial State", Status.DELETED);

        Map<Integer, TodoState> timeline = new HashMap<>();
        timeline.put(1, initialState);
        Todo original = new Todo(uuid.toString(), timeline);

        Todo expected = original;
        original.getTodoTimeline().put(2, updateState);

        // When

        Todo actual = original.setStateDeleted();

        // Then
        assertEquals(expected, actual);
    }

}