package com.infy.retail.perkspal.utils;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    private static final Logger commonUtilsLogger = LoggerFactory.getLogger(CommonUtils.class);
    /**
     * This method creates a new reward object each time and saves them into the DB by calculating the Rewards points by the purchase made
     * @param customerList    the CustomerList contains list of Customers entity
     * @param rewardService  its a dependency used to save the rewards in the DB
     * @throws PerksPalException if there is an error while saving the transaction
     */
    public static void calculateRewards(List<Customer> customerList, RewardService rewardService) throws PerksPalException {
        commonUtilsLogger.debug("CommonUtils.calculateRewards() Starts ");
        try {
            List<Reward> rewardsList = new ArrayList<>();
            customerList.forEach(customer ->
            {
                customer.getRetailTransactions().forEach(
                        transactionStream -> {
                            Reward reward = new Reward();
                            try {
                                reward.setPoints(calculatePoints(transactionStream.getPrice()));
                            } catch (PerksPalException e) {
                                throw new RuntimeException(e);
                            }
                            reward.setDate(transactionStream.getDate());
                            reward.setCustomer(transactionStream.getCustomer());
                            rewardsList.add(reward);
                        }
                );
            });
            rewardService.saveAllRewards(rewardsList);
            commonUtilsLogger.debug("CommonUtils.calculateRewards() Ends ");

        } catch (Exception commonUtilsException) {
            throw new PerksPalException("unable to asses the rewards",commonUtilsException);
        }
    }

    private static int calculatePoints(double price) throws PerksPalException {
        commonUtilsLogger.debug("CommonUtils.calculatePoints() Starts ");
        try { int points = 0;
        if (price > 100) {
            points += (int) ((price - 100) * 2 + 50);
        } else if (price > 50) {
            points += (int) (price - 50);
        }
        commonUtilsLogger.debug("CommonUtils.calculatePoints() ends ");
        return points;
    } catch (Exception commonUtilsException) {
        throw new PerksPalException("unable to asses the rewards",commonUtilsException);
    }
    }
}
