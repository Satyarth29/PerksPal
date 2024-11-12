package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import jakarta.persistence.EntityManager;
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
    @Transactional
    public void  saveTransaction(CustomerDTO customerDTO) throws PerksPalException {
        try {
            if(ObjectUtils.isEmpty(customerDTO))
                throw new PerksPalException("the input is empty");
            Customer customer = new Customer();
            customer.setName(customerDTO.name());
            customer = customerService.saveCustomer(customer);
            RetailTransaction transaction = new RetailTransaction();
            transaction.setDate(customerDTO.date());
            transaction.setPrice(customerDTO.price());
            transaction.setCustomer(customer);
            transactionRepository.save(transaction);
            //to refresh the entity
            entityManager.refresh(customer);

            calculateRewards(customerService, rewardService);
        } catch (Exception e) {
            throw new PerksPalException(e.getMessage(),e.getCause());
        }
    }

    public void saveAllTransactions(List<RetailTransaction> retailTransactionList) throws PerksPalException {
        try {
            transactionRepository.saveAll(retailTransactionList);
        }
        catch (Exception e){
            throw new PerksPalException(e.getMessage(),e.getCause());
        }
    }
}
