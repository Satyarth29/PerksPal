package com.infy.retail.perkspal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.exceptionhandler.GlobalExceptionHandler;
import com.infy.retail.perkspal.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardController.class)
@ContextConfiguration(classes = {RewardController.class, GlobalExceptionHandler.class})
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive test case for getRewardsByMonth
    @Test
    void getRewardsByMonth_success() throws Exception {
        // Arrange
        Long customerId = 1L;
        Map<Month, Integer> rewardsPerMonth = new HashMap<>();
        rewardsPerMonth.put(Month.JANUARY, 50);
        rewardsPerMonth.put(Month.FEBRUARY, 30);

        when(rewardService.getRewardsPerMonth(customerId)).thenReturn(rewardsPerMonth);

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/month/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rewardsPerMonth)));

        verify(rewardService, times(1)).getRewardsPerMonth(customerId);
    }

    // Negative test case for getRewardsByMonth - Exception scenario
    @Test
    void getRewardsByMonth_exception() throws Exception {
        // Arrange
        Long customerId = 1L;
        when(rewardService.getRewardsPerMonth(customerId)).thenThrow(new PerksPalException("Unable to fetch rewards"));

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/month/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to fetch rewards"));

        verify(rewardService, times(1)).getRewardsPerMonth(customerId);
    }

    // Positive test case for getTotalRewards
    @Test
    void getTotalRewards_success() throws Exception {
        // Arrange
        Long customerId = 1L;
        Integer totalRewards = 100;

        when(rewardService.getAllRewards(customerId)).thenReturn(totalRewards);

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/all/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        verify(rewardService, times(1)).getAllRewards(customerId);
    }

    // Negative test case for getTotalRewards - Exception scenario
    @Test
    void getTotalRewards_exception() throws Exception {
        // Arrange
        Long customerId = 1L;
        when(rewardService.getAllRewards(customerId)).thenThrow(new PerksPalException("Unable to calculate total rewards"));

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/all/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to calculate total rewards"));

        verify(rewardService, times(1)).getAllRewards(customerId);
    }
}
