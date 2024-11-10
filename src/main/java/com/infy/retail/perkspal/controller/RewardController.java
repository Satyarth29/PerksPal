package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/calculate/{id}")
    public ResponseEntity<String> calculateRewards(@PathVariable Long id) {
        rewardService.calculateRewards(id);
        return ResponseEntity.ok("Rewards calculated successfully");
    }


}
