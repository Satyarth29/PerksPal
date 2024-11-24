package com.infy.retail.perkspal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.retail.perkspal.dto.CustomerRequestDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.exceptionhandler.GlobalExceptionHandler;
import com.infy.retail.perkspal.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@WebMvcTest(TransactionController.class)
@ContextConfiguration(classes = {TransactionController.class, GlobalExceptionHandler.class})
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void commitTransaction_successfulTransaction() throws Exception {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("samwell", 234.0, LocalDate.of(2043,12,21));
        String json = """
                {
                "name":"samwell",
                "price":234.0,
                "date":"2043-12-21"
                }
                """;
        doNothing().when(transactionService).saveTransaction(customerRequestDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction completed successfully"));

        verify(transactionService, times(1)).saveTransaction(customerRequestDTO);
    }

    @Test
    void commitTransaction_nullPrice_throwsPerksPalException() throws Exception {
        // Arrange
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("Satyarth",null,null);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("please buy to proceed!!"));

        verify(transactionService, never()).saveTransaction(customerRequestDTO);
    }

    // Negative test case: exception thrown from service layer
    @Test
    void commitTransaction_serviceThrowsException_throwsPerksPalException() throws Exception {
        // Arrange
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("",100.0,null);

        doThrow(new PerksPalException("Service exception")).when(transactionService).saveTransaction(customerRequestDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Service exception"));

        verify(transactionService, times(1)).saveTransaction(customerRequestDTO);
    }

}