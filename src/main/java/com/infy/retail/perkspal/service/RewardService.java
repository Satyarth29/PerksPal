package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerResponseDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.respository.RewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardService {
    private final RewardRepository rewardRepository;

    public RewardService(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    private final Logger rewardsServiceLogger = LoggerFactory.getLogger(RewardService.class);

    /**
     * Invokes the DB for fetching all rewards by  customerID and starting and ending dates.
     * And merges the points into a map by the date as a key.
     * @param id        the ID of the customer whose rewards are to be retrieved
     * @param startDate the start date of the range (defaults to three months ago if not provided)
     * @param endDate   the end date of the range (defaults to the current date if not provided)
     * @return a ResponseEntity containing the CustomerResponseDTO with the rewards information
     * @throws PerksPalException if there is an error retrieving the rewards
     */
    public CustomerResponseDTO getRewardsInRange(Long id, LocalDate startDate, LocalDate endDate) throws PerksPalException {
        rewardsServiceLogger.info("RewardService.getRewardsInRange() starts with id: {} startDate: {} endDate:{}", id, startDate, endDate);
        try {
            List<Reward> rewardList = rewardRepository.findAllByCustomerIdAndDateBetween(id, startDate, endDate);
            validateRewardList(rewardList);
            Map<String,Integer> totalRewardsMap = new HashMap<>();
            for (Reward reward : rewardList) {
                LocalDate date = reward.getDate();
                Integer points = reward.getPoints();
                totalRewardsMap.merge(date.toString(), points, Integer::sum);
            }
            CustomerResponseDTO customerResponseDTO = prepareCustomerResponseDTO(rewardList, totalRewardsMap);
            rewardsServiceLogger.info("RewardService.getRewardsInRange() ends with customerResponseDTO: {}", customerResponseDTO);
            return customerResponseDTO;
        } catch (Exception e) {
            throw new PerksPalException(e.getMessage());
        }
    }

    /**
     * Retrieves the all rewards for a customer by customer ID by invoking DB and sums them before storing into a map.
     *
     * @param id the ID of the customer whose rewards are to be retrieved
     * @return a ResponseEntity containing the CustomerResponseDTO with the rewards information
     * @throws PerksPalException if there is an error retrieving the rewards
     */
    public CustomerResponseDTO getAllRewards(Long id) throws PerksPalException {
        try {
            rewardsServiceLogger.info("*************RewardService.getAllRewards starts with id: {}***************", id);

            List<Reward> rewardList = getRewardsList(id);
            validateRewardList(rewardList);
            Integer totalPoints = rewardList.stream().mapToInt(Reward::getPoints)
                    .sum();
            HashMap<String,Integer> totalRewardsMap = new HashMap<>(){};
            totalRewardsMap.put("total Rewards : ",totalPoints);
            CustomerResponseDTO customerResponseDTO = prepareCustomerResponseDTO(rewardList, totalRewardsMap);
            rewardsServiceLogger.info("*************RewardService.getAllRewards ends with customerResponse: {}*************", customerResponseDTO);
            return customerResponseDTO;
        } catch (Exception e) {
            throw new PerksPalException("Error Processing", e.getCause());
        }
    }

    private void validateRewardList(List<Reward> rewardList) throws PerksPalException {
        if (ObjectUtils.isEmpty(rewardList)) throw new PerksPalException("No rewards Found");
    }

    public List<Reward> getRewardsList(Long id) throws PerksPalException {
        rewardsServiceLogger.info("RewardService.getRewardsList() starts with id: {} ", id);
        try {
            List<Reward> rewardList = rewardRepository.findAllByCustomerId(id);
            return rewardList;
        } catch (RuntimeException e) {
            throw new PerksPalException("Database error", e.getCause());
        }

    }

    private CustomerResponseDTO prepareCustomerResponseDTO(List<Reward> rewardList, Map<String,Integer> totalRewards) {
        rewardsServiceLogger.info("RewardService.prepareCustomerResponseDTO() starts with totalRewards: {} ", totalRewards);
        return new CustomerResponseDTO(rewardList.get(0).getCustomer().getName(), totalRewards);
    }
    /**
     * @param rewardList      the rewardsList contains list of reward entity to be saved in the DB
     * @throws PerksPalException if there is an error while saving the reward
     */
    public void saveAllRewards(List<Reward> rewardList) {
        rewardsServiceLogger.info("RewardService.saveAllRewards starts with rewardList: {}", rewardList);
        rewardRepository.saveAll(rewardList);
        rewardsServiceLogger.info("RewardService.saveAllRewards ends");
    }

}
