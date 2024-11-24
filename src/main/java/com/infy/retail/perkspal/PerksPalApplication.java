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
	/**
	 * This project aims to create a RESTful application with endpoints to manage customers, transactions, and rewards.
	 * The application's database will store reward information after calculating the rewards.
	 * @Author Satyarth Sharma
	 */
	public static void main(String[] args) {
		SpringApplication.run(PerksPalApplication.class, args);
	}
}

