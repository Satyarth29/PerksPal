package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.TransactionPayload;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private RetailTransactionRepository transactionRepository;
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionPayload transactionPayload;
    private Customer customer;
    private RetailTransaction transaction;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionPayload = new TransactionPayload(1L, 120.0);
        customer = new Customer();
        customer.setId(transactionPayload.id());
        customer.setName("John Doe");
        transaction = new RetailTransaction();
        transaction.setDate(LocalDate.now());
        transaction.setPrice(transactionPayload.price());
        transaction.setCustomer(customer);
        customer.setRetailTransactions(List.of(transaction));
    }

    @Test
    public void saveTransaction_PositiveFlow()  {
        when(transactionRepository.save(any(RetailTransaction.class))).thenReturn(transaction);
        when(customerService.findById(any())).thenReturn(Optional.ofNullable(customer));

        transactionService.saveTransaction(transactionPayload);
        verify(transactionRepository, times(1)).save(any(RetailTransaction.class));  // Ensure save was called once
        verify(customerService, times(1)).findById(any(Long.class));  // Ensure saveCustomer was called once

    }
    @Test
    public void saveAllTransaction_PositiveFlow()  {
        when(transactionRepository.saveAll(any(List.class))).thenReturn(List.of(transaction));

        transactionService.saveAllTransactions(List.of(transaction));
        verify(transactionRepository, times(1)).saveAll(any(List.class));  // Ensure save was called once
    }

    @Test
    public void testSaveTransaction_ThrowsPerks_Pal_Exception_When_Payload_Is_Null() {
        // Arrange
        TransactionPayload transactionPayload = null;

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.saveTransaction(transactionPayload);
        });

        // Verify the exception message
        assertEquals("Cannot invoke \"com.infy.retail.perkspal.dto.TransactionPayload.id()\" because \"transactionPayload\" is null", exception.getMessage());
    }

    @Test
    public void saveAllTransaction_NegativeFlow_SaveFails()  {
        doThrow(new RuntimeException("Transaction did not commit due to :"))
                .when(transactionRepository).saveAll(anyList());

        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.saveAllTransactions(List.of());
        });
        assertEquals("Transaction did not commit due to :", exception.getMessage());
    }
    @Test
    void testSaveTransaction_ResourceNotFoundException() {
        // Arrange
        TransactionPayload transactionPayload = new TransactionPayload(123L, 100.0); // Example payload
        when(customerService.findById(transactionPayload.id())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.saveTransaction(transactionPayload);
        });

        // Verify the exception message
        assertEquals("Customer Not Found while processing transaction", exception.getMessage());

        // Verify interactions
        verify(customerService).findById(transactionPayload.id());
        verifyNoInteractions(transactionRepository); // Ensure repository is not called
    }


}