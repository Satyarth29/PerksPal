package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.respository.CustomerRepository;
import com.infy.retail.perkspal.respository.RewardRepository;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardService {
    private final RetailTransactionRepository retailTransactionRepository;
    private final RewardRepository rewardRepository;
    private final CustomerRepository customerRepository;


    public RewardService(RetailTransactionRepository retailTransactionRepository, RewardRepository rewardRepository, CustomerRepository customerRepository) {
        this.retailTransactionRepository = retailTransactionRepository;
        this.rewardRepository = rewardRepository;
        this.customerRepository = customerRepository;
    }

    public Map<String,Double> getRewardsPerMonth(Long id) throws PerksPalException {
                    calculateRewards(id);
                    Map<String,Double> rewardsPerMonth = new HashMap<>();
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.getRetailTransactions().forEach(
                transactionStream -> {
                    Reward reward = new Reward();
                    reward.setPoints(calculatePoints(transactionStream.getPrice()));
                    reward.setDate(transactionStream.getDate());
                    reward.setCustomer(transactionStream.getCustomer());
                    rewardRepository.save(reward);
                }
        );
    }

    public Double getAllRewards(Long id){

    }

    private void calculateRewards(Long id) throws PerksPalException {

    }


    private  int calculatePoints(double price) {
        int points = 0;
        if (price > 100) {
            points += (price - 100) * 2 + 50;
        } else if (price > 50) {
            points += (price - 50);
        }
        return points;
    }

}
