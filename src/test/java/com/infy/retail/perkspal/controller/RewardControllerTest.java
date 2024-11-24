package com.infy.retail.perkspal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.retail.perkspal.dto.CustomerResponseDTO;
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

import java.time.LocalDate;
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
    CustomerResponseDTO customerResponseDTO;
    Long customerId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String,Integer> totalRewardsPointsMap = new HashMap<>();
        totalRewardsPointsMap.put(LocalDate.of(2024,8,20).toString(),4);
        totalRewardsPointsMap.put(LocalDate.of(2024,11,20).toString(),4);
        customerResponseDTO = new CustomerResponseDTO("jhonny depp",totalRewardsPointsMap);

    }

    // Positive test case for getRewardsInRange
    @Test
    void getRewardsInRange_success() throws Exception {
        // Arrange
        when(rewardService.getRewardsInRange(any(), any(),any())).thenReturn(customerResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/range/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponseDTO)));

        verify(rewardService, times(1)).getRewardsInRange(customerId,LocalDate.now().minusMonths(3),LocalDate.now());
    }

    // Negative test case for getRewardsInRange - Exception scenario
    @Test
    void getRewardsInRange_exception() throws Exception {
        // Arrange
        when(rewardService.getRewardsInRange(any(),any(),any())).thenThrow(new PerksPalException("Unable to fetch rewards"));

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/range/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unable to fetch rewards"));

        verify(rewardService, times(1)).getRewardsInRange(customerId,LocalDate.now().minusMonths(3),LocalDate.now());
    }

    // Positive test case for getTotalRewards
    @Test
    void getTotalRewards_success() throws Exception {
        // stub
        when(rewardService.getAllRewards(customerId)).thenReturn(customerResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/all/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponseDTO)));

        verify(rewardService, times(1)).getAllRewards(customerId);
    }

    // Negative test case for getTotalRewards - Exception scenario
    @Test
    void getTotalRewards_exception() throws Exception {
        // Arrange
        when(rewardService.getAllRewards(customerId)).thenThrow(new PerksPalException("Unable to calculate total rewards"));

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/all/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unable to calculate total rewards"));

        verify(rewardService, times(1)).getAllRewards(customerId);
    }
}
