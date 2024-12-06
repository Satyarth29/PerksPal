package com.infy.retail.perkspal.config;

import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.service.CustomerService;
import com.infy.retail.perkspal.service.RewardService;
import com.infy.retail.perkspal.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {
    private final Logger dataInitializerLogger = LoggerFactory.getLogger(DataInitializer.class);

    private final CustomerService customerService;
    private final RewardService rewardService;
    private final TransactionService transactionService;

    public DataInitializer(CustomerService customerService, RewardService rewardService, TransactionService transactionService) {
        this.customerService = customerService;
        this.rewardService = rewardService;
        this.transactionService = transactionService;
    }


/**
 * Data Initializer preloads some data so that
 * the GET API can be used with the start for the application
 * */
    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            dataInitializerLogger.debug("DataInitialization starts");
            // Create customers
            Customer customer1 = new Customer();
            customer1.setName("swati");
            customer1 = customerService.saveCustomer(customer1);

            Customer customer2 = new Customer();
            customer2.setName("satyarth");
            customer2 = customerService.saveCustomer(customer2);

            Customer customer3 = new Customer();
            customer3.setName("madhav");
            customer3 = customerService.saveCustomer(customer3);

            Customer customer4 = new Customer();
            customer4.setName("shree");
            customer4 = customerService.saveCustomer(customer4);
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
            retailTransaction4.setDate(LocalDate.of(2024, 11, 5));
            retailTransaction4.setPrice(45.0);
            RetailTransaction retailTransaction5 = new RetailTransaction();
            retailTransaction5.setDate(LocalDate.of(2024, 9, 14));
            retailTransaction5.setPrice(100.0);

            //saving all transactions
            transactionService.saveAllTransactions(List.of(
                    retailTransaction1,
                    retailTransaction2,
                    retailTransaction3,
                    retailTransaction4
            ));
            dataInitializerLogger.debug("DataInitialization ends");
//            rewardService.calculateRewards(customerService.findAllCustomers());
        };
    }

}