package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerRequestDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;

class TransactionServiceTest {
    @Mock
    private RetailTransactionRepository transactionRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private RewardService rewardService;
    @InjectMocks
    private TransactionService transactionService;

    private CustomerRequestDTO customerRequestDTO;
    private Customer customer;
    private RetailTransaction transaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRequestDTO = new CustomerRequestDTO("John Doe", 120.0, LocalDate.of(2024, 11, 8));
        customer = new Customer();
        customer.setName(customerRequestDTO.name());
        transaction = new RetailTransaction();
        transaction.setDate(customerRequestDTO.date());
        transaction.setPrice(customerRequestDTO.price());
        transaction.setCustomer(customer);
        customer.setRetailTransactions(List.of(transaction));
    }

    @Test
    public void saveTransaction_PositiveFlow() throws PerksPalException {
        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);
        when(transactionRepository.save(any(RetailTransaction.class))).thenReturn(transaction);
        doNothing().when(entityManager).refresh(customer);  // Mock refresh

        transactionService.saveTransaction(customerRequestDTO);

        verify(customerService, times(1)).saveCustomer(any(Customer.class));  // Ensure saveCustomer was called once
        verify(transactionRepository, times(1)).save(any(RetailTransaction.class));  // Ensure save was called once
        verify(entityManager, times(1)).refresh(any(Customer.class));  // Ensure refresh was called once
    }

    @Test
    public void testSaveTransaction_ThrowsPerks_Pal_Exception_When_Customer_Is_Empty() {
        // Arrange
        CustomerRequestDTO customerRequestDTO = null;

        // Act & Assert
        Exception exception = assertThrows(PerksPalException.class, () -> {
            transactionService.saveTransaction(customerRequestDTO);
        });

        // Verify the exception message
        assertEquals("the input is empty", exception.getMessage());
    }

    @Test
    public void saveTransaction_NegativeFlow_SaveFails() throws PerksPalException {
        when(customerService.saveCustomer(any(Customer.class))).thenThrow(new RuntimeException("Database error"));
        PerksPalException exception = assertThrows(PerksPalException.class, () -> {
            transactionService.saveTransaction(customerRequestDTO);
        });
        assertEquals("Database error", exception.getMessage());
    }


}