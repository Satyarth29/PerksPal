package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.respository.CustomerRepository;
import com.infy.retail.perkspal.respository.RewardRepository;
import com.infy.retail.perkspal.respository.RetailTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Month;
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

    public Map<Month, Integer> getRewardsPerMonth(Long id) throws PerksPalException {
        calculateRewards(id);
        Map<Month, Integer> rewardsPerMonthMap = new HashMap<>();
        List<Reward> rewardList = getRewardsList(id);
        for (Reward reward : rewardList) {
            Month month = reward.getDate().getMonth();
            Integer points = reward.getPoints();
            rewardsPerMonthMap.merge(month, points, Integer::sum);
        }
        return rewardsPerMonthMap;
    }

    public Integer getAllRewards(Long id) throws PerksPalException {
        calculateRewards(id);
        List<Reward> rewardList = getRewardsList(id);
        Integer totalPoints = rewardList.stream().mapToInt(Reward::getPoints)
                .sum();
        return totalPoints;
    }

    private List<Reward> getRewardsList(Long id) {
        List<Reward> rewardList = rewardRepository.findAllByCustomerId(id);
        return rewardList;
    }


    private void calculateRewards(Long id) throws PerksPalException {
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


    private int calculatePoints(double price) {
        int points = 0;
        if (price > 100) {
            points += (price - 100) * 2 + 50;
        } else if (price > 50) {
            points += (price - 50);
        }
        return points;
    }

}
