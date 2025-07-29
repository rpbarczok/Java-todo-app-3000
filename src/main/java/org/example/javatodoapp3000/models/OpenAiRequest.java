package org.example.javatodoapp3000.models;

import java.util.List;

public record OpenAiRequest(
        String model,
        List<OpenAiMessages> messages
) {
}
