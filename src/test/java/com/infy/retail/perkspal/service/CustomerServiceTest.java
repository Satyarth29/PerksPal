package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.exceptions.ResourceNotFoundException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.respository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceTest {
    @InjectMocks
    CustomerService customerService;
    @Mock
    CustomerRepository customerRepository;

    private Customer customer;
    private final String exceptedExceptionMsg = "database error";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Satyarth");
    }

    @Test
    void saveCustomer()  {
        when(customerRepository.save(any())).thenReturn(customer);
        customerService.saveCustomer(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void findByIDCustomers() {
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        Optional<Customer> customer1 = customerService.findById(1L);
        assertEquals(customer, customer1.get());
    }

    @Test
    void saveCustomerException() {
        when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException(exceptedExceptionMsg));
        Exception exception = assertThrows(RuntimeException.class, () ->
                customerService.saveCustomer(customer)
        );
        assertEquals(exceptedExceptionMsg, exception.getMessage());
    }

    @Test
    void findByIDCustomerException() {
       when(customerRepository.findById(any())).thenThrow(new RuntimeException(exceptedExceptionMsg));
       Exception exception = assertThrows(RuntimeException.class,()->
               customerService.findById(1L));
       assertEquals(exceptedExceptionMsg,exception.getMessage());
    }
}