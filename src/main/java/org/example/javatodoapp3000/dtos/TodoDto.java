package org.example.javatodoapp3000.dtos;

import org.example.javatodoapp3000.utils.Status;

public record TodoDto(String id, String description, Status status) {
}
