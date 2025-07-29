package org.example.javatodoapp3000.dtos;

import lombok.With;
import org.example.javatodoapp3000.utils.Status;

@With
public record TodoDto(String id, String description, Status status) {
}
