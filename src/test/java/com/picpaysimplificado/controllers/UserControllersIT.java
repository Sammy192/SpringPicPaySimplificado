package com.picpaysimplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.entities.Transaction;
import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllersIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    private Long existingId, nonExistingId, dependentId, existingNotDependentId;
    private String existingDocument, nontExistingDocument;

    private UserDTO existingUserDTO, newUserDTO;
    private User existingUser, newUser;


    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 1L;
        existingNotDependentId = 3L;
        existingDocument = "1234999222";
        nontExistingDocument = "12349992233";

        existingUser = new User(null,"Mateus", "Silva","1234999222","mateus@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, new ArrayList<>(), new ArrayList<>(), null);
        existingUserDTO = new UserDTO(existingUser);

        newUser = new User(null,"Mick", "Silva","1234888222","mick@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, new ArrayList<>(), new ArrayList<>(), null);
        newUserDTO = new UserDTO(newUser);

    }


    @Test
    public void getAllUsersShouldReturnListOfUsers() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/users")
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").value(1L));
        result.andExpect(jsonPath("$[0].firstName").value("Mateus"));
        result.andExpect(jsonPath("$[0].lastName").value("Silva"));
        result.andExpect(jsonPath("$[0].document").value("1234999222"));
        result.andExpect(jsonPath("$[0].balance").value(100.00));
        result.andExpect(jsonPath("$[0].email").value("mateus@example.com"));
        result.andExpect(jsonPath("$[0].password").value("123456"));
        result.andExpect(jsonPath("$[0].userType").value("COMMON"));
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/users/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.firstName").value("Mateus"));
        result.andExpect(jsonPath("$.lastName").value("Silva"));
        result.andExpect(jsonPath("$.document").value("1234999222"));
        result.andExpect(jsonPath("$.balance").value(100.00));
        result.andExpect(jsonPath("$.email").value("mateus@example.com"));
        result.andExpect(jsonPath("$.password").value("123456"));
        result.andExpect(jsonPath("$.userType").value("COMMON"));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void createUserShouldReturnUserDTOCreated() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(newUserDTO);

        ResultActions result =
                mockMvc.perform(post("/users")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(5L));
        result.andExpect(jsonPath("$.firstName").value("Mick"));
        result.andExpect(jsonPath("$.lastName").value("Silva"));
        result.andExpect(jsonPath("$.document").value("1234888222"));
        result.andExpect(jsonPath("$.balance").value(100.00));
        result.andExpect(jsonPath("$.email").value("mick@example.com"));
        result.andExpect(jsonPath("$.password").value("123456"));
        result.andExpect(jsonPath("$.userType").value("COMMON"));
    }

    @Test
    public void createUserShouldReturnBadRequestWhenUserAlreadyExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(existingUserDTO);

        ResultActions result =
                mockMvc.perform(post("/users")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createUserShouldReturnUnprocessableEntityWhenFirstNameIsEmpty() throws Exception {

        User novoUser = new User(null," ", "Silva","1234888222","mick@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO novoUserDTO = new UserDTO(novoUser);


        String jsonBody = objectMapper.writeValueAsString(novoUser);

        ResultActions result =
                mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("firstName"));
        result.andExpect(jsonPath("$.errors[0].message").value("Campo obrigatório"));
    }

    @Test
    public void createUserShouldReturnUnprocessableEntityWhenLastNameIsEmpty() throws Exception {

        User novoUser = new User(null,"Fernanda", " ","1234888222","mick@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO novoUserDTO = new UserDTO(novoUser);


        String jsonBody = objectMapper.writeValueAsString(novoUser);

        ResultActions result =
                mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("lastName"));
        result.andExpect(jsonPath("$.errors[0].message").value("Campo obrigatório"));
    }

    @Test
    public void createUserShouldReturnUnprocessableEntityWhenEmailIsNotValid() throws Exception {

        User novoUser = new User(null,"Fernanda", "Silva","1234888222","mickexample.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO novoUserDTO = new UserDTO(novoUser);


        String jsonBody = objectMapper.writeValueAsString(novoUser);

        ResultActions result =
                mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
        result.andExpect(jsonPath("$.errors[0].message").value("Informe um email válido."));
    }

    @Test
    public void updateShouldReturnUserDTOWhenIdExists() throws Exception {

        User updateExistingUser = new User(null,"Mateus atualizado", "Silva","1234999222","mateus@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO updateExistingUserDTO = new UserDTO(updateExistingUser);

        String jsonBody = objectMapper.writeValueAsString(updateExistingUserDTO);

        ResultActions result =
                mockMvc.perform(put("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.firstName").value("Mateus atualizado"));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        User updateNonExistingUser = new User(null,"Mateus atualizado", "Silva","1234999222","mateus@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO updateNonExistingUserDTO = new UserDTO(updateNonExistingUser);

        String jsonBody = objectMapper.writeValueAsString(updateNonExistingUserDTO);

        ResultActions result =
                mockMvc.perform(put("/users/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenFirstNameIsEmpty() throws Exception {

        User novoUser = new User(null," ", "Silva","1234888222","mick@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO novoUserDTO = new UserDTO(novoUser);

        String jsonBody = objectMapper.writeValueAsString(novoUserDTO);

        ResultActions result =
                mockMvc.perform(put("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenLastNameIsEmpty() throws Exception {

        User novoUser = new User(null,"Fernanda", " ","1234888222","mick@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO novoUserDTO = new UserDTO(novoUser);

        String jsonBody = objectMapper.writeValueAsString(novoUserDTO);

        ResultActions result =
                mockMvc.perform(put("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenEmailIsNotValid() throws Exception {

        User novoUser = new User(null,"Fernanda", "Silva","1234888222","mickexample.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        UserDTO novoUserDTO = new UserDTO(novoUser);

        String jsonBody = objectMapper.writeValueAsString(novoUserDTO);

        ResultActions result =
                mockMvc.perform(put("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {

        ResultActions result =
                mockMvc.perform(delete("/users/{id}", existingNotDependentId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result =
                mockMvc.perform(delete("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenIdExistsAndExistsTransactions() throws Exception {

        Transaction transaction = new Transaction(null, new BigDecimal("10.00"), existingUser, newUser, Instant.now());

        ResultActions result =
                mockMvc.perform(delete("/users/{id}", transaction.getSender().getId())
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void findUserByDocumentShouldReturnUserDTOWhenIdExists() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/users/document/{id}", existingDocument)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.firstName").value("Mateus"));
        result.andExpect(jsonPath("$.lastName").value("Silva"));
        result.andExpect(jsonPath("$.document").value("1234999222"));
        result.andExpect(jsonPath("$.balance").value(100.00));
        result.andExpect(jsonPath("$.email").value("mateus@example.com"));
        result.andExpect(jsonPath("$.password").value("123456"));
        result.andExpect(jsonPath("$.userType").value("COMMON"));
    }

    @Test
    public void findUserByDocumentShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/users/document/{id}", nontExistingDocument)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}
