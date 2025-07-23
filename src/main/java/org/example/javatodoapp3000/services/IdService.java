package org.example.javatodoapp3000.services;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Value
public class IdService {

    public String generateId () {
        return UUID.randomUUID().toString();
    }
}
