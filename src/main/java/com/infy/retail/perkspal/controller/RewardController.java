package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.dto.CustomerResponseDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger rewardsControllerLogger = LoggerFactory.getLogger(RewardController.class);

    /**
     * Retrieves the all rewards for a customer by customer ID.
     *
     * @param id the ID of the customer whose rewards are to be retrieved
     * @return a ResponseEntity containing the CustomerResponseDTO with the rewards information
     * @throws PerksPalException if there is an error retrieving the rewards
     * @author Satyarth Sharma
     */
    @GetMapping("/calculate/all/{id}")
    public ResponseEntity<CustomerResponseDTO> getTotalRewards(@PathVariable Long id) throws PerksPalException {
      rewardsControllerLogger.info("************* RewardController.getTotalRewards start with customerID: {} *************",id);
        CustomerResponseDTO customerResponseDTO = rewardService.getAllRewards(id);
        return ResponseEntity.ok(customerResponseDTO);
    }
    /**
     * Retrieves the rewards for a customer within a specified date range.
     * If no date range is provided, it defaults to the last three months from current date.
     *
     * @param id        the ID of the customer whose rewards are to be retrieved
     * @param startDate the start date of the range (defaults to three months ago if not provided)
     * @param endDate   the end date of the range (defaults to the current date if not provided)
     * @return a ResponseEntity containing the CustomerResponseDTO with the rewards information
     * @throws PerksPalException if there is an error retrieving the rewards
     * @author Satyarth Sharma
     */
    @GetMapping("/calculate/range/{id}")
    public ResponseEntity<CustomerResponseDTO>
    getRewardsInRange(@PathVariable Long id,
                      @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().minusMonths(3)}")
                      LocalDate startDate,
                      @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
                      LocalDate endDate) throws PerksPalException {
        rewardsControllerLogger.info("RewardController.getRewardsInRange() starts with id: {} startDate: {} endDate:{}", id, startDate, endDate);
        CustomerResponseDTO customerResponseDTO = rewardService.getRewardsInRange(id, startDate, endDate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Message", "Congratulations! here your rewards");
        return new ResponseEntity<>(customerResponseDTO, headers, HttpStatus.OK);
    }


}
