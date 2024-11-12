package com.infy.retail.perkspal.utils;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.service.CustomerService;
import com.infy.retail.perkspal.service.RewardService;

import java.util.List;

public class CommonUtils {
    public static void calculateRewards(CustomerService customerService, RewardService rewardService) throws PerksPalException {
        List<Customer> customerList = customerService.findAllCustomers();

          customerList.forEach(customer ->
        {
            customer.getRetailTransactions().forEach(
                    transactionStream -> {
                        Reward reward = new Reward();
                        reward.setPoints(calculatePoints(transactionStream.getPrice()));
                        reward.setDate(transactionStream.getDate());
                        reward.setCustomer(transactionStream.getCustomer());
                        rewardService.saveReward(reward);
                    }
            );
        });
    }

    private static int calculatePoints(double price) {
        int points = 0;
        if (price > 100) {
            points += (price - 100) * 2 + 50;
        } else if (price > 50) {
            points += (price - 50);
        }
        return points;
    }
}
