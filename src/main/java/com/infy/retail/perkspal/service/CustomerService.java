package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.respository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final Logger customerServiceLogger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    /**
     * @param customer      the saves customer entity into the DB
     * @throws PerksPalException if there is an error while saving the transaction
     */
    public Customer saveCustomer(Customer customer) throws PerksPalException {
        customerServiceLogger.info("CustomerService.saveCustomer starts for customer:{}",customer);
        try {
            Customer customer1 = customerRepository.save(customer);
            customerServiceLogger.info("CustomerService.saveCustomer ends with customerID: {}",customer.getId());
            return customer1;
        } catch (RuntimeException e) {
            throw new PerksPalException("database error", e);
        }
    }
    /**
     * finds All customer from the DB
     * @throws PerksPalException if there is an error while saving the transaction
     */
    public List<Customer> findAllCustomers() throws PerksPalException {
        try {
            customerServiceLogger.info("CustomerService.findAllCustomers starts");
            return customerRepository.findAll();
        } catch (RuntimeException e) {
            throw new PerksPalException("database error", e);

        }
    }
}
