package org.example.javatodoapp3000.services;

import lombok.Value;
import org.example.javatodoapp3000.models.OpenAiResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(RestClient.Builder restClientBuilder,
                         @org.springframework.beans.factory.annotation.Value("${API_KEY") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String autoCorrectString(String string) {
        String requestBody = String.format("""
        {
            "model": "o4-mini",
            "messages": [
                {
                    "role": "user",
                    "content": "Bitte den folgenden Text auf Rechschreibung prüfen und die korrigierte Fassung zurückgeben und zwar nur die korrigierte Fassung, ohne irgendwelche Anmerkungen: %s"
                }
            ]
        }
        """, string);
        OpenAiResponse response =  restClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(OpenAiResponse.class);
        return response.choices().get(0).messages().content();
    }
}
