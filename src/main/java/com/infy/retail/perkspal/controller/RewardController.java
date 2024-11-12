package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/calculate/month/{id}")
    public ResponseEntity<Map<Month,Integer>> getRewardsByMonth(@PathVariable Long id) throws PerksPalException {
        Map<Month,Integer> map =  rewardService.getRewardsPerMonth(id);
        return ResponseEntity.ok(map);
    }
    @GetMapping("/calculate/all/{id}")
    public ResponseEntity<Integer> getTotalRewards(@PathVariable Long id) throws PerksPalException {
     Integer totalRewards =   rewardService.getAllRewards(id);
        return ResponseEntity.ok(totalRewards);
    }

}
