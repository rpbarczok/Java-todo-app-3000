package org.example.javatodoapp3000.services;

import lombok.RequiredArgsConstructor;
import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.exceptions.NotFoundException;
import org.example.javatodoapp3000.models.Todo;
import org.example.javatodoapp3000.repository.TodoRepo;
import org.example.javatodoapp3000.utils.Status;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {
        private final TodoRepo todoRepo;

        public List<TodoDto> findAllTodos() {
            List<Todo> todos = todoRepo.findAll();
            List<TodoDto> todosDto = new ArrayList<>();
            for (Todo todo : todos) {
                TodoDto todoDto = todo.getLatestStateAsTodoDto();
                if (todoDto.status() != Status.DELETED) {
                    todosDto.add(todoDto);
                }
            }
            return todosDto;
        }

        public TodoDto addTodo(TodoDto todoDto) {
            Todo todo = new Todo(UUID.randomUUID().toString(), todoDto);
            Todo newTodo = todoRepo.save(todo);
            return newTodo.getLatestStateAsTodoDto();
        }

        public TodoDto findTodoById(String id) {
            Todo todo = todoRepo.findTodoById(id);
            return todo.getLatestStateAsTodoDto();
        }

        public TodoDto updateTodo(TodoDto todoDto) throws NotFoundException {
            Todo todo = todoRepo.findTodoById(todoDto.id());
            if (todo != null) {
                Todo newTodo = todo.updateTodo(todoDto);
                Todo updatedTodo = todoRepo.save(newTodo);
                return updatedTodo.getLatestStateAsTodoDto();
            } else {
                throw new NotFoundException("Todo item with id " + todoDto.id() + " not found.");
            }
        }

        public void setTodoToDeleted(String id) throws NotFoundException {
            Todo todo = todoRepo.findTodoById(id);
            if (todo != null) {
                Todo deletedTodo = todo.setStateDeleted();
                todoRepo.save(deletedTodo);
            } else {
                throw new NotFoundException("Todo item with id " + id + " not found.");
            }
        }
}
