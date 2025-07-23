package org.example.javatodoapp3000.models;

import lombok.With;
import org.example.javatodoapp3000.utils.Status;

import java.time.Instant;

@With
public record TodoState(String description, Status status) {

}
