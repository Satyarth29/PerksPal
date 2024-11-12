package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private CustomerDTO customerDTO;
    private Customer customer;
    private RetailTransaction transaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO("John Doe", 120.0, LocalDate.of(2024, 11, 8));
        customer = new Customer();
        customer.setName(customerDTO.name());
        transaction = new RetailTransaction();
        transaction.setDate(customerDTO.date());
        transaction.setPrice(customerDTO.price());
        transaction.setCustomer(customer);
    }

    @Test
    public void saveTransaction_PositiveFlow() throws PerksPalException {
        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);
        when(transactionRepository.save(any(RetailTransaction.class))).thenReturn(transaction);
        doNothing().when(entityManager).refresh(any(Customer.class));  // Mock refresh

        transactionService.saveTransaction(customerDTO);

        verify(customerService, times(1)).saveCustomer(any(Customer.class));  // Ensure saveCustomer was called once
        verify(transactionRepository, times(1)).save(any(RetailTransaction.class));  // Ensure save was called once
        verify(entityManager, times(1)).refresh(any(Customer.class));  // Ensure refresh was called once
    }

    @Test
    public void testSaveTransaction_ThrowsPerks_Pal_Exception_When_Customer_Is_Empty() {
        // Arrange
        CustomerDTO customerDTO = null;

        // Act & Assert
        Exception exception = assertThrows(PerksPalException.class, () -> {
            transactionService.saveTransaction(customerDTO);
        });

        // Verify the exception message
        assertEquals("the input is empty", exception.getMessage());
    }

    @Test
    public void saveTransaction_NegativeFlow_SaveFails() throws PerksPalException {
        when(customerService.saveCustomer(any(Customer.class))).thenThrow(new RuntimeException("Database error"));
        PerksPalException exception = assertThrows(PerksPalException.class, () -> {
            transactionService.saveTransaction(customerDTO);
        });
        assertEquals("Database error", exception.getMessage());
    }
}