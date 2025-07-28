package org.example.javatodoapp3000.controllers;


import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.exceptions.NotFoundException;
import org.example.javatodoapp3000.services.TodoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
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
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto postTodo(@RequestBody TodoDto todoDto) {
        return todoService.addTodo(todoDto);
    }

    @GetMapping("/{id}")
    public TodoDto getTodo(@PathVariable String id) throws NotFoundException {
        return todoService.findTodoById(id);
    }

    @PutMapping("/{id}")
    public TodoDto putTodo(@PathVariable String id, @RequestBody TodoDto todoDto) throws NotFoundException {
        return todoService.updateTodo(todoDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable String id) throws NotFoundException {
        todoService.setTodoToDeleted(id);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>("Error 404: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
