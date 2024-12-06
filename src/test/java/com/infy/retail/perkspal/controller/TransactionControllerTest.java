package com.infy.retail.perkspal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.retail.perkspal.dto.TransactionPayload;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


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
    void testCommitTransaction_Success() throws Exception {
        // Arrange: Define a valid TransactionPayload
        TransactionPayload transactionPayload = new TransactionPayload(123L, 150.0);

        // Convert the payload to JSON
        String payloadJson = new ObjectMapper().writeValueAsString(transactionPayload);

        // Mock service behavior
        doNothing().when(transactionService).saveTransaction(any(TransactionPayload.class));

        // Act: Perform a POST request
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                // Assert: Validate response
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction completed successfully"));

        // Verify that the service was called with the correct payload
        verify(transactionService).saveTransaction(transactionPayload);
    }

    @Test
    void commitTransaction_nullPrice_throwsExceptionWhenPriceIsNull() throws Exception {
        // Arrange
        TransactionPayload transactionPayload = new TransactionPayload(1L,null);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input: please provide valid ID or price "));

        verify(transactionService, never()).saveTransaction(transactionPayload);
    }

    // Negative test case: exception thrown from service layer
    @Test
    void commitTransaction_throwsException_whenIdIsNull() throws Exception {
        // Arrange
        TransactionPayload transactionPayload = new TransactionPayload(null,100.0);


        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input: please provide valid ID or price "));

        verify(transactionService,  never()).saveTransaction(transactionPayload);
    }

}