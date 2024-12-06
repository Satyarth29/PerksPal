package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.respository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @param customer the saves customer entity into the DB
     */
    public Customer saveCustomer(Customer customer) {
        log.debug("customer onBoarding initiated for :{}", customer);
        customer = customerRepository.save(customer);
        log.debug("customer onBoarding successfull: {}", customer.getId());
        return customer;
    }

    public Optional<Customer> findById(Long Id) {
        log.debug("finding customer for: {}",Id);
        return customerRepository.findById(Id);
    }
}
