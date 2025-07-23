package org.example.javatodoapp3000.controllers;

import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.services.TodoService;
import org.example.javatodoapp3000.utils.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest{
    static UUID uuid = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Test
    void getAllTodos_returns_empty_list_when_empty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
                //then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    };

    @Test
    void getAllTodos_returns_list_of_one_todo() throws Exception {
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        //Given
        todoService.addTodo(new TodoDto(uuid.toString(), "Hallo", Status.OPEN));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                //then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                          [
                              {
                                  "description": "Hallo",
                                  "status": "OPEN"
                              }
                          ]
"""));
    };

    @Test
    void postTodo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "description": "Hallo World",
                          "status": "OPEN"
                         }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}