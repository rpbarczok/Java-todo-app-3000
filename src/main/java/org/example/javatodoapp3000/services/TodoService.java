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

@Service
@RequiredArgsConstructor
public class TodoService {
        private final TodoRepo todoRepo;
        private final IdService idService;

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
            Todo todo = new Todo(todoDto.id() != null ? todoDto.id() : idService.generateId(), todoDto);
            Todo newTodo = todoRepo.save(todo);
            return newTodo.getLatestStateAsTodoDto();
        }

        public TodoDto findTodoById(String id) throws NotFoundException {
            try {
                Todo todo = todoRepo.findTodoById(id);
                TodoDto foundTodoDto = todo.getLatestStateAsTodoDto();
                if (foundTodoDto.status() == Status.DELETED) {
                    throw new NotFoundException("Todo with id " + id + " not found");
                } else {
                    return foundTodoDto;
                }
            } catch (Exception ex) {
                throw new NotFoundException("Todo with id " + id + " not found.");
            }
        }

        public TodoDto updateTodo(TodoDto todoDto) throws NotFoundException {
            Todo oldTodo = todoRepo.findTodoById(todoDto.id());
            if (oldTodo != null) {
                TodoDto oldTodoDto = oldTodo.getLatestStateAsTodoDto();
                if (oldTodoDto.status() == Status.DELETED) {
                    throw new NotFoundException("Todo item with id " +  todoDto.id() + " not found");
                } else {
                    Todo newTodo = oldTodo.updateTodo(todoDto);
                    Todo updatedTodo = todoRepo.save(newTodo);
                    return updatedTodo.getLatestStateAsTodoDto();
                }
            } else {
                throw new NotFoundException("Todo item with id " + todoDto.id() + " not found.");
            }
        }

        public void setTodoToDeleted(String id) throws NotFoundException {
            Todo oldTodo = todoRepo.findTodoById(id);
            if (oldTodo != null) {
                TodoDto oldTodoDto  = oldTodo.getLatestStateAsTodoDto();
                if (oldTodoDto.status() == Status.DELETED) {
                    throw new NotFoundException("Todo item with id " + id + " not found");
                } else {
                    Todo deletedTodo = oldTodo.setStateDeleted();
                    todoRepo.save(deletedTodo);
                }
            } else {
                throw new NotFoundException("Todo item with id " + id + " not found.");
            }
        }
}
