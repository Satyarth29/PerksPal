package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerRequestDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.infy.retail.perkspal.utils.CommonUtils.calculateRewards;

@Service
public class TransactionService {
    private final EntityManager entityManager;
    private final RetailTransactionRepository transactionRepository;

    private final CustomerService customerService;

    private final RewardService rewardService;

    public TransactionService(EntityManager entityManager, RetailTransactionRepository transactionRepository, CustomerService customerService, RewardService rewardService) {
        this.entityManager = entityManager;
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
        this.rewardService = rewardService;
    }
    private final Logger transactionalServiceLogger =  LoggerFactory.getLogger(TransactionService.class);
    /**
     * saving customer,transaction,reward entities by invoking customerRepo,RetailTransactionRepo,RewardRepo respectively
     * And refreshes the entity to get the saved data by eager fetching
     * @param customerRequestDTO       the customerDTO contains
     * @throws PerksPalException if there is an error while saving the transaction
     */
    @Transactional
    public void  saveTransaction(CustomerRequestDTO customerRequestDTO) throws PerksPalException {
        try {
            transactionalServiceLogger.info("TransactionService.saveTransaction starts for customerDTO : {}",customerRequestDTO);
            if(ObjectUtils.isEmpty(customerRequestDTO))
                throw new PerksPalException("the input is empty");
            Customer customer = new Customer();
            customer.setName(customerRequestDTO.name());
            customer = customerService.saveCustomer(customer);

            RetailTransaction transaction = new RetailTransaction();
            transaction.setDate(customerRequestDTO.date());
            transaction.setPrice(customerRequestDTO.price());
            transaction.setCustomer(customer);
            transactionRepository.save(transaction);
            //to refresh the entity
            entityManager.refresh(customer);
            calculateRewards(List.of(customer), rewardService);
        } catch (Exception e) {
            throw new PerksPalException(e.getMessage(),e.getCause());
        }
    }
    /**
     * @param retailTransactionList       the retailTransactionList contains list of transactions entity to be saved in the DB
     * @throws PerksPalException if there is an error while saving the transaction
     */
    public void saveAllTransactions(List<RetailTransaction> retailTransactionList) throws PerksPalException {
        try {
            transactionalServiceLogger.info("TransactionService.saveALlTransaction starts for retailList : {}",retailTransactionList);
            transactionRepository.saveAll(retailTransactionList);
            transactionalServiceLogger.info("TransactionService.saveALlTransaction ends");
        }
        catch (Exception e){
            throw new PerksPalException(e.getMessage(),e.getCause());
        }
    }
}
