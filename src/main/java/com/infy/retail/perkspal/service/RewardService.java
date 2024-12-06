package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerPointsRecord;
import com.infy.retail.perkspal.dto.LoyaltyRewardResponse;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.exceptions.ResourceNotFoundException;
import com.infy.retail.perkspal.exceptions.RewardsCalculationException;
import com.infy.retail.perkspal.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RewardService {
    private final CustomerService customerService;

    public RewardService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Invokes the DB for fetching all rewards by  customerID and starting and ending dates.
     * And merges the point into a map by the date as a key.
     *
     * @param id        the ID of the customer whose rewards are to be retrieved
     * @param startDate the start date of the range (defaults to three months ago if not provided)
     * @param endDate   the end date of the range (defaults to the current date if not provided)
     * @return a ResponseEntity containing the LoyaltyRewardResponse with the rewards information
     * @ if there is an error retrieving the rewards
     */
    public LoyaltyRewardResponse getRewardsInRange(Long id, LocalDate startDate, LocalDate endDate) {
        log.info("generating the rewards for id :{},startDate :{} and endDate:{}", id, startDate, endDate);
            List<CustomerPointsRecord> customerPointsRecordList = getRewardListByID(id);
            customerPointsRecordList = customerPointsRecordList.stream()
                                            .filter(customerPointsRecord ->
                                                    !customerPointsRecord.date().isBefore(startDate) && !customerPointsRecord.date().isAfter(endDate)).toList();

            Map<String, Integer> totalRewardsMap = new HashMap<>();
            for (CustomerPointsRecord customerPointsRecord : customerPointsRecordList) {
                LocalDate date = customerPointsRecord.date();
                Integer points = customerPointsRecord.point();
                totalRewardsMap.merge(date.toString(), points, Integer::sum);
            }
            LoyaltyRewardResponse loyaltyRewardResponse = generateRewardsResponse(customerPointsRecordList, totalRewardsMap);
            log.info("rewards between the given transaction range successfully fetched: {}", loyaltyRewardResponse);
            return loyaltyRewardResponse;
    }
    /**
     * Retrieves the all rewards for a customer by customer ID by invoking DB and sums them before storing into a map and returning a DTO as a response.
     *
     * @param id the ID of the customer whose rewards are to be retrieved
     * @return a ResponseEntity containing the LoyaltyRewardResponse with the rewards information
     */
    public LoyaltyRewardResponse getAllRewards(Long id){
            log.info("fetching all rewards for the customerID: {}", id);
            List<CustomerPointsRecord> customerPointsRecordList = getRewardListByID(id);
            Integer totalPointsPerCustomer = customerPointsRecordList.stream().mapToInt(CustomerPointsRecord::point)
                    .sum();
            HashMap<String, Integer> totalRewardsMap = new HashMap<>() {
            };
            totalRewardsMap.put("Customers total Rewards : ", totalPointsPerCustomer);
            LoyaltyRewardResponse loyaltyRewardResponse = generateRewardsResponse(customerPointsRecordList, totalRewardsMap);
            log.info("all rewards fetched: {}", loyaltyRewardResponse);
            return loyaltyRewardResponse;
    }

    /**
     * Retrieves the rewardsList By ID for a customer by invoking DB and calls to generate rewards.
     *
     * @param id the ID of the customer whose rewards are to be retrieved
     * @return a list of CustomerPointsRecord having list of customers and their corresponding points
     */
    private List<CustomerPointsRecord> getRewardListByID(Long id) {
        log.debug("Invoking Repository to fetch customer for customerID:{}",id);
        Customer customer = customerService.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer not Found"));
        List<CustomerPointsRecord> customerPointsRecordList = generateRewards(customer);
        if (ObjectUtils.isEmpty(customerPointsRecordList)) throw new ResourceNotFoundException("No rewards Found");
        return customerPointsRecordList;
    }

    /**
     * This method creates a new reward object each time and saves the into the
     * DB by calculating the Rewards point by the purchase made
     * @param customer the param contains customer object
     */
    public List<CustomerPointsRecord> generateRewards(Customer customer) throws RewardsCalculationException {
        log.debug("generating the rewards and building the reward DTO");

        return customer.getRetailTransactions().stream()
                .map(transaction -> {
                    try {
                        return new CustomerPointsRecord(
                                transaction.getCustomer(),
                                calculatePoints(transaction.getPrice()),
                                transaction.getDate());
                    } catch (Exception e) {
                        throw new RewardsCalculationException("unable to asses the rewards",e);
                    }
                })
                .collect(Collectors.toList());
    }

    private int calculatePoints(double price) throws PerksPalException {
        log.debug("point calculation commenced");
        try {
            int points = 0;
            if (price > 100) {
                points += (int) ((price - 100) * 2 + 50);
            } else if (price > 50) {
                points += (int) (price - 50);
            }
            log.debug("point calculation successfully completed");
            return points;
        } catch (Exception e) {
            throw new RewardsCalculationException("unable to asses the rewards",e);
        }
    }
    private LoyaltyRewardResponse generateRewardsResponse(List<CustomerPointsRecord> customerPointsRecordList, Map<String, Integer> totalRewards) {
        log.debug("creating response with : {}", totalRewards);
        return new LoyaltyRewardResponse(customerPointsRecordList.get(0).customer().getName(), totalRewards);
    }
}
