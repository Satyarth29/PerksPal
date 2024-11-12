package com.infy.retail.perkspal.config;

import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.service.CustomerService;
import com.infy.retail.perkspal.service.RewardService;
import com.infy.retail.perkspal.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static com.infy.retail.perkspal.utils.CommonUtils.calculateRewards;

@Configuration
public class DataInitializer {
    private final CustomerService customerService;
    private final RewardService rewardService;
    private final TransactionService transactionService;

    public DataInitializer(CustomerService customerService, RewardService rewardService, TransactionService transactionService) {
        this.customerService = customerService;
        this.rewardService = rewardService;
        this.transactionService = transactionService;
    }


    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Create customers
            Customer customer1 = new Customer();
            customer1.setName("sharma");
            customer1 = customerService.saveCustomer(customer1);

            Customer customer2 = new Customer();
            customer2.setName("satyarth");
            customer2 = customerService.saveCustomer(customer2);

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
            calculateRewards(customerService,rewardService);
        };
    }




}
