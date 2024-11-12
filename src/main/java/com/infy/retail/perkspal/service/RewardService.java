package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.respository.RewardRepository;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardService {
    private final RewardRepository rewardRepository;

    public RewardService(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    public Map<Month, Integer> getRewardsPerMonth(Long id) throws PerksPalException {
      try {
          Map<Month, Integer> rewardsPerMonthMap = new HashMap<>();
          List<Reward> rewardList = getRewardsList(id);
          for (Reward reward : rewardList) {
              Month month = reward.getDate().getMonth();
              Integer points = reward.getPoints();
              rewardsPerMonthMap.merge(month, points, Integer::sum);
          }
          return rewardsPerMonthMap;
      }
      catch (Exception ex){
          throw new PerksPalException(ex.getMessage(),ex.getCause());
      }

    }

    public Integer getAllRewards(Long id) throws PerksPalException {
        List<Reward> rewardList = getRewardsList(id);
        Integer totalPoints = rewardList.stream().mapToInt(Reward::getPoints)
                .sum();
        return totalPoints;
    }
    public List<Reward> getRewardsList(Long id) throws PerksPalException {
        try {
            List<Reward> rewardList = rewardRepository.findAllByCustomerId(id);
            return rewardList;
        }
        catch (RuntimeException e){
            throw new PerksPalException("Database error",e.getCause());
        }

    }
    public void saveReward(Reward reward){
        rewardRepository.save(reward);
    }

}
