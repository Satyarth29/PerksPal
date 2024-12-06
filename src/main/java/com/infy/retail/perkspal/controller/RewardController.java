package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.dto.LoyaltyRewardResponse;
import com.infy.retail.perkspal.exceptions.InvalidInputException;
import com.infy.retail.perkspal.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }


    /**
     * Retrieves the all rewards for a customer by customer ID.
     *
     * @param id the ID of the customer whose rewards are to be retrieved
     * @return a ResponseEntity containing the LoyaltyRewardResponse with the rewards information
     * @author Satyarth Sharma
     */
    @GetMapping("/calculate/all/{id}")
    public ResponseEntity<LoyaltyRewardResponse> getTotalRewards(@PathVariable Long id) {
        LoyaltyRewardResponse loyaltyRewardResponse = rewardService.getAllRewards(id);
        return ResponseEntity.ok(loyaltyRewardResponse);
    }
    /**
     * Retrieves the rewards for a customer within a specified date range.
     * If no date range is provided, it defaults to the last three months from current date.
     *
     * @param id        the ID of the customer whose rewards are to be retrieved
     * @param startDate the start date of the range (defaults to three months ago if not provided)
     * @param endDate   the end date of the range (defaults to the current date if not provided)
     * @return a ResponseEntity containing the LoyaltyRewardResponse with the rewards information
     * @author Satyarth Sharma
     */
    @GetMapping("/calculate/range/{id}")
    public ResponseEntity<LoyaltyRewardResponse>
    getRewardsInRange(@PathVariable Long id,
                      @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().minusMonths(3)}")
                      LocalDate startDate,
                      @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
                      LocalDate endDate) {
        LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);

        validateInput(id, startDate, endDate, twentyYearsAgo);
        LoyaltyRewardResponse loyaltyRewardResponse = rewardService.getRewardsInRange(id, startDate, endDate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Message", "Congratulations! here your rewards");
        return new ResponseEntity<>(loyaltyRewardResponse, headers, HttpStatus.OK);
    }

    private void validateInput(Long id, LocalDate startDate, LocalDate endDate, LocalDate twentyYearsAgo) {
        if (startDate.isBefore(twentyYearsAgo)) {
            throw new InvalidInputException("Start date cannot be earlier than " + twentyYearsAgo);
        }

        // Validation: Dates should not be in the future
        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new InvalidInputException("Dates cannot be in the future");
        }

        // Validation: End date should not be before start date
        if (endDate.isBefore(startDate)) {
            throw new InvalidInputException("End date cannot be before start date");
        }
        if (id == null || id == 0.0){
            throw new InvalidInputException("ID cannot be null or zero");
        }
    }


}
