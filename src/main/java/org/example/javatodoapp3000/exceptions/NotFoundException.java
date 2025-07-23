package org.example.javatodoapp3000.exceptions;

import org.example.javatodoapp3000.dtos.TodoDto;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
}
