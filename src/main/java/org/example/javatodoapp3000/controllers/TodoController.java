package org.example.javatodoapp3000.controllers;


import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.exceptions.NotFoundException;
import org.example.javatodoapp3000.services.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {this.todoService = todoService;}

    @GetMapping
    public List<TodoDto> getAllTodos() {
        return todoService.findAllTodos();
    }

    @PostMapping
    public TodoDto postTodo(@RequestBody TodoDto todoDto) {
        return todoService.addTodo(todoDto);
    }

    @GetMapping("/{id}")
    public TodoDto getTodo(@PathVariable String id) {
        return todoService.findTodoById(id);
    }

    @PutMapping("/{id}")
    public TodoDto putTodo(@PathVariable String id, @RequestBody TodoDto todoDto) {
        try {
            return todoService.updateTodo(todoDto);
        } catch (NotFoundException nfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nfe.getMessage());
        }

    }
}
