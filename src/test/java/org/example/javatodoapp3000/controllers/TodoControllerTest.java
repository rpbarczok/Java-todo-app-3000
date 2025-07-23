package org.example.javatodoapp3000.controllers;

import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.services.IdService;
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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Autowired
    private IdService idService;

    @Test
    void getAllTodos_returns_empty_list_when_empty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
                //then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    };

    @Test
    void getAllTodos_returns_list_of_one_todo() throws Exception {
        //Given
        todoService.addTodo(new TodoDto(idService.generateId(), "Hallo", Status.OPEN));

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
    void postTodo_returns_new_todo() throws Exception {
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

    @Test
    void getMappingId_gets_specific_todo_id() throws Exception {

    }

    @Test
    void getMappingId_throws_exception_when_todo_exists() throws Exception {

    }

}