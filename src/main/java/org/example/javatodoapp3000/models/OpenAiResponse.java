package org.example.javatodoapp3000.models;


import java.util.List;

public record OpenAiResponse(List<OpenAiChoice> choices) {
    public List<OpenAiChoice> choices() {
        return null;
    }
}
