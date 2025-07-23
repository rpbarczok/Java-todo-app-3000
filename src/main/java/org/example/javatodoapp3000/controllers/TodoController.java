package org.example.javatodoapp3000.controllers;

import org.example.javatodoapp3000.Todo;
import org.example.javatodoapp3000.repository.TodoRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @GetMapping
    public List<Todo> getAllTodos() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

}
