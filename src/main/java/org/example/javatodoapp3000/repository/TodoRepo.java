package org.example.javatodoapp3000.repository;

import org.example.javatodoapp3000.models.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepo extends MongoRepository<Todo, String> {

    public Todo findTodoById(String id);

}
