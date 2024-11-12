package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.respository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer) throws PerksPalException {
        try {
            return customerRepository.save(customer);
        } catch (RuntimeException e) {
            throw new PerksPalException("database error", e);
        }
    }

    public List<Customer> findAllCustomers() throws PerksPalException {
        try {
            return customerRepository.findAll();
        } catch (RuntimeException e) {
            throw new PerksPalException("database error", e);

        }
    }
}
