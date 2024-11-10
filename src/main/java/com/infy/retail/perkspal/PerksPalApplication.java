package com.infy.retail.perkspal;

import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.CustomerRepository;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class PerksPalApplication {
private final RetailTransactionRepository retailTransactionRepository;
private final CustomerRepository customerRepository;

	public PerksPalApplication(RetailTransactionRepository retailTransactionRepository, CustomerRepository customerRepository) {
		this.retailTransactionRepository = retailTransactionRepository;
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(PerksPalApplication.class, args);
	}
}

