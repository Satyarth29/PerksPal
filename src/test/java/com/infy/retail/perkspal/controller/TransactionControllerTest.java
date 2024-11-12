package com.infy.retail.perkspal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.retail.perkspal.dto.CustomerDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.exceptionhandler.GlobalExceptionHandler;
import com.infy.retail.perkspal.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        CustomerDTO customerDTO = new CustomerDTO("John Doe", 100.0, LocalDate.now());

        doNothing().when(transactionService).saveTransaction(customerDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction completed successfully"));

        verify(transactionService, times(1)).saveTransaction(customerDTO);
    }

    @Test
    void commitTransaction_nullPrice_throwsPerksPalException() throws Exception {
        // Arrange
        CustomerDTO customerDTO = new CustomerDTO("Satyarth",null,null);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("please buy to proceed!!"));

        verify(transactionService, never()).saveTransaction(customerDTO);
    }

    // Negative test case: exception thrown from service layer
    @Test
    void commitTransaction_serviceThrowsException_throwsPerksPalException() throws Exception {
        // Arrange
        CustomerDTO customerDTO = new CustomerDTO("",100.0,null);

        doThrow(new PerksPalException("Service exception")).when(transactionService).saveTransaction(customerDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Service exception"));

        verify(transactionService, times(1)).saveTransaction(customerDTO);
    }

}