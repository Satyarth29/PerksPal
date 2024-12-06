package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.TransactionPayload;
import com.infy.retail.perkspal.exceptions.InvalidInputException;
import com.infy.retail.perkspal.exceptions.ResourceNotFoundException;
import com.infy.retail.perkspal.exceptions.TransactionFailedException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class TransactionService {
    private final RetailTransactionRepository transactionRepository;
    private final CustomerService customerService;

    public TransactionService(RetailTransactionRepository transactionRepository, CustomerService customerService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
    }

    /**
     * saving transaction entities by invoking RetailTransactionRepo
     *
     * @param transactionPayload the customerDTO contains
     */
    public void saveTransaction(TransactionPayload transactionPayload) {
        log.debug("committing the transaction into the DB : {}", transactionPayload);
        try {
            Customer customer = customerService.findById(transactionPayload.id()).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found while processing transaction"));
            RetailTransaction transaction = new RetailTransaction();
            transaction.setDate(LocalDate.now());
            transaction.setPrice(transactionPayload.price());
            transaction.setCustomer(customer);
            transactionRepository.save(transaction);
            log.debug("Transaction successfully committed into the DB");
        } catch (RuntimeException e) {
            throw new TransactionFailedException("Transaction did not commit due to :", e);
        }
    }

    /**
     * @param retailTransactionList the retailTransactionList contains list of transactions entity to be saved in the DB
     */
    public void saveAllTransactions(List<RetailTransaction> retailTransactionList) {
        log.debug("committing the transaction into the DB : {}", retailTransactionList);
        try {
            transactionRepository.saveAll(retailTransactionList);
            log.debug("Transaction successfully committed into the DB");
        } catch (RuntimeException e) {
            throw new TransactionFailedException("Transaction did not commit due to :", e);
        }
    }
}
