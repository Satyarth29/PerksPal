package com.infy.retail.perkspal.config;

import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.CustomerRepository;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import jakarta.transaction.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository, RetailTransactionRepository transactionRepository) {
        return args -> {
            // Create customers
            Customer customer1 = new Customer();
            customer1.setName("swati");
            customer1 = customerRepository.save(customer1);

            Customer customer2 = new Customer();
            customer2.setName("satyarth");
            customer2 = customerRepository.save(customer2);

            // Create transactions for customer 1
            RetailTransaction retailTransaction1 = new RetailTransaction();
            retailTransaction1.setDate(LocalDate.of(2024, 8, 15));
            retailTransaction1.setCustomer(customer1);
            retailTransaction1.setPrice(120.0);
            RetailTransaction retailTransaction2 = new RetailTransaction();
            retailTransaction2.setDate(LocalDate.of(2024, 10, 10));
            retailTransaction2.setCustomer(customer1);
            retailTransaction2.setPrice(75.0);

            //for customer 2
            RetailTransaction retailTransaction3 = new RetailTransaction();
            RetailTransaction retailTransaction4 = new RetailTransaction();
            retailTransaction3.setCustomer(customer2);
            retailTransaction3.setDate(LocalDate.of(2024, 9, 25));
            retailTransaction3.setPrice(150.0);
            retailTransaction4.setCustomer(customer2);
            retailTransaction4.setDate( LocalDate.of(2024, 11, 5));
            retailTransaction4.setPrice(45.0);

            //saving all transactions
            transactionRepository.saveAll(List.of(
                    retailTransaction1,
                    retailTransaction2,
                    retailTransaction3,
                    retailTransaction4
            ));
        };
    }
}
