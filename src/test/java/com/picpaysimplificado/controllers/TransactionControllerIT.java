package com.picpaysimplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpaysimplificado.dtos.TransactionDTO;
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

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId, nonExistingId;
    private String existingDocument, nontExistingDocument;

    private Transaction transaction;

    private TransactionDTO transactionDTO;

    private UserDTO existingUserDTO, newUserDTO;
    private User existingUser, newUser;


    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 1000L;

        existingDocument = "1234999222";
        nontExistingDocument = "12349992233";

        transactionDTO = new TransactionDTO(1L,2L,new BigDecimal(10));

    }


    @Test
    public void createTransactionShouldReturnDoneTransactionDTOCreatedWhenTransactionIsAuthorized() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(transactionDTO);

        ResultActions result =
                mockMvc.perform(post("/transactions")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.amount").value(10.00));
        result.andExpect(jsonPath("$.sender").exists());
        result.andExpect(jsonPath("$.receiver").exists());
    }

    @Test
    public void createTransactionShouldReturnForbiddenWhenTransactionIsNotAuthorized() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(transactionDTO);

        ResultActions result =
                mockMvc.perform(post("/transactions")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isForbidden());
        result.andExpect(jsonPath("$.status").value(403));
        result.andExpect(jsonPath("$.error").value("Transação não autorizada"));
        result.andExpect(jsonPath("$.path").value("/transactions"));
    }

    @Test
    public void createTransactionShouldReturnUnprocessableEntityWhenSenderIdIsNotInformed() throws Exception {

        TransactionDTO transactionDTO1 = new TransactionDTO(null, 2L,new BigDecimal(10));

        String jsonBody = objectMapper.writeValueAsString(transactionDTO1);

        ResultActions result =
                mockMvc.perform(post("/transactions")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createTransactionShouldReturnUnprocessableEntityWhenReceiverIdIsNotInformed() throws Exception {

        TransactionDTO transactionDTO1 = new TransactionDTO(1L, null,new BigDecimal(10));

        String jsonBody = objectMapper.writeValueAsString(transactionDTO1);

        ResultActions result =
                mockMvc.perform(post("/transactions")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createTransactionShouldReturnUnprocessableEntityWhenAmountIsNotInformed() throws Exception {

        TransactionDTO transactionDTO1 = new TransactionDTO(1L, 2L,null);

        String jsonBody = objectMapper.writeValueAsString(transactionDTO1);

        ResultActions result =
                mockMvc.perform(post("/transactions")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createTransactionShouldReturnUnprocessableEntityWhenAmountIszero() throws Exception {

        TransactionDTO transactionDTO1 = new TransactionDTO(1L, 2L,new BigDecimal(0));

        String jsonBody = objectMapper.writeValueAsString(transactionDTO1);

        ResultActions result =
                mockMvc.perform(post("/transactions")
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getAllTransactionsShouldReturnListOfTransactions() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/transactions")
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").value(1L));
        result.andExpect(jsonPath("$[0].amount").exists());
        result.andExpect(jsonPath("$[0].sender").exists());
        result.andExpect(jsonPath("$[0].receiver").exists());
        result.andExpect(jsonPath("$[1].id").value(2L));
        result.andExpect(jsonPath("$[1].amount").exists());
        result.andExpect(jsonPath("$[1].sender").exists());
        result.andExpect(jsonPath("$[1].receiver").exists());
    }

    @Test
    public void getTransactionsByUserIdShouldReturnListOfTransactionsWhenUserIdExists() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/transactions/user/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").exists());
        result.andExpect(jsonPath("$[0].amount").exists());
        result.andExpect(jsonPath("$[0].sender.id", anyOf(is(1),is(2))));
        result.andExpect(jsonPath("$[0].receiver.id", anyOf(is(1),is(2))));
    }

    @Test
    public void getTransactionsByUserIdShouldReturnListOfTransactionWhenUserIdNotExists() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/transactions/user/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void getTransactionsByUserDocumentItShouldReturnListOfTransactionsWhenUserDocumentExists() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/transactions/user/document/{id}", existingDocument)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").exists());
        result.andExpect(jsonPath("$[0].amount").exists());
        result.andExpect(jsonPath("$[0].sender.id", anyOf(is(1),is(2))));
        result.andExpect(jsonPath("$[0].receiver.id", anyOf(is(1),is(2))));
    }

    @Test
    public void getTransactionsByUserDocumentItShouldReturnListOfTransactionsWhenUserDocumentNotExists() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/transactions/user/document/{id}", nontExistingDocument)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$").isEmpty());
    }
}
