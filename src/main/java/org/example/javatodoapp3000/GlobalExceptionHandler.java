package org.example.javatodoapp3000;

import org.example.javatodoapp3000.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        if (Objects.equals(System.getProperty("Config"), "PROD")) {
            return new ResponseEntity<>("Error 500: Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }  else {
            return new ResponseEntity<>("Error 500: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
