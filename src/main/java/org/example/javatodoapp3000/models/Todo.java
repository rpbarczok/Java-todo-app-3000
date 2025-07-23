package org.example.javatodoapp3000.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.utils.Status;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Todo {
    @Getter
    String id;
    int processCounter = 0;
    @Getter
    Map<Integer, TodoState> todoTimeline = new HashMap<>();

    public Todo(String id, String description, Status status) {
        this.id = id;
        processCounter += 1;
        todoTimeline.put(processCounter, new TodoState(description, status));
    }

    public Todo(String id, TodoDto todoDto) {
        this.id = id;
        processCounter += 1;
        todoTimeline.put(processCounter, new TodoState(todoDto.description(), todoDto.status()));
    }

    public Todo(String id, Map<Integer, TodoState> todoTimeline) {
        this.id = id;
        processCounter += todoTimeline.size();
        this.todoTimeline = todoTimeline;
    }

    public TodoDto getLatestStateAsTodoDto() {
        int k = 0;
        for(int key : this.getTodoTimeline().keySet() ) {
            if (key > k) {
                k = key;
            }
        }
        return new TodoDto(this.getId(), this.getTodoTimeline().get(k).description(), this.getTodoTimeline().get(k).status());

    }

    public Todo updateTodo(TodoDto todoDto) {
        processCounter += 1;
        todoTimeline.put(processCounter, new TodoState(todoDto.description(), todoDto.status()));
        return this;
    }

}
