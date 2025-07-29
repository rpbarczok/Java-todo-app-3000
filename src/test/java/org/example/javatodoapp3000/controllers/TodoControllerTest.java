package org.example.javatodoapp3000.controllers;

import org.example.javatodoapp3000.dtos.TodoDto;
import org.example.javatodoapp3000.services.ChatGPTService;
import org.example.javatodoapp3000.services.IdService;
import org.example.javatodoapp3000.services.TodoService;
import org.example.javatodoapp3000.utils.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

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

    @MockitoBean
    private ChatGPTService chatGPTService;

    @Test
    void getAllTodos_returns_empty_list_when_empty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                //then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    };

    @Test
    void getAllTodos_returns_list_of_one_todo() throws Exception {
        when(chatGPTService.autoCorrectString("Hallo")).thenReturn("Hallo");
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
        when(chatGPTService.autoCorrectString("Hallo World")).thenReturn("Hallo world");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "description": "Hallo World",
                          "status": "OPEN"
                         }
                        """))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void getTodo_by_id_gets_specific_todo_id() throws Exception {
        when(chatGPTService.autoCorrectString("Hallo")).thenReturn("Hallo");

        todoService.addTodo(new TodoDto("1234", "Hallo", Status.OPEN));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/1234"))
                //then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                              {
                                  "description": "Hallo",
                                  "status": "OPEN"
                              }
                """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void getTodo_by_Id_throws_404_when_todo_does_not_exist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/fail"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getTodo_by_Id_throws_404_when_todo_does_was_deleted() throws Exception {
        todoService.addTodo(new TodoDto("1234", "Hallo", Status.DELETED));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/1234"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void  putTodo_returns_updated_todo() throws Exception {
        when(chatGPTService.autoCorrectString("Hallo World")).thenReturn("Hallo world");
        todoService.addTodo(new TodoDto("1234", "Hallo", Status.OPEN));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                 "id": "1234",
                                 "description": "Hallo World",
                                 "status": "IN_PROGRESS"
                                 }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                                "id": "1234",
                                "description": "Hallo world",
                                "status": "IN_PROGRESS"
                        }
                        """));
    }

    @Test
    void  putTodo_throws_throws_404_when_todo_does_not_exist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/fail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "id": "fail",
                          "description": "Hallo World",
                          "status": "OPEN"
                         }
                        """))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void  putTodo_throws_throws_404_when_todo_is_deleted() throws Exception {
        //Given
        todoService.addTodo(new TodoDto("1234", "Hallo", Status.DELETED));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "id": "1234",
                          "description": "Hallo World",
                          "status": "IN_PROGRESS"
                         }
                        """))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTodo_succeeds_when_todo_exists() throws Exception {
        todoService.addTodo(new TodoDto("1234", "Hallo", Status.OPEN));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1234"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void deleteTodo_returns_404_when_called_with_non_existing_todo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1234"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTodo_returns_404_when_called_with_deleted_todo() throws Exception {
        todoService.addTodo(new TodoDto("1234", "Hallo", Status.DELETED));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1234"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}