package com.infy.retail.perkspal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.retail.perkspal.dto.LoyaltyRewardResponse;
import com.infy.retail.perkspal.exceptions.InvalidInputException;
import com.infy.retail.perkspal.exceptionhandler.GlobalExceptionHandler;
import com.infy.retail.perkspal.exceptions.RewardsCalculationException;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    LoyaltyRewardResponse loyaltyRewardResponse;
    Long customerId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String,Integer> totalRewardsPointsMap = new HashMap<>();
        totalRewardsPointsMap.put(LocalDate.of(2024,8,20).toString(),4);
        totalRewardsPointsMap.put(LocalDate.of(2024,11,20).toString(),4);
        loyaltyRewardResponse = new LoyaltyRewardResponse("jhonny depp",totalRewardsPointsMap);

    }

    // Positive test case for getRewardsInRange
    @Test
    void getRewardsInRange_success() throws Exception {
        // Arrange
        when(rewardService.getRewardsInRange(any(), any(),any())).thenReturn(loyaltyRewardResponse);

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/range/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loyaltyRewardResponse)));

        verify(rewardService, times(1)).getRewardsInRange(customerId,LocalDate.now().minusMonths(3),LocalDate.now());
    }

    // Negative test case for getRewardsInRange - Exception scenario
    @Test
    void getRewardsInRange_exception() throws Exception {
        // Arrange
        when(rewardService.getRewardsInRange(any(),any(),any())).thenThrow(new InvalidInputException("ID cannot be null or zero"));

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/range/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("ID cannot be null or zero"));

        verify(rewardService, times(1)).getRewardsInRange(customerId,LocalDate.now().minusMonths(3),LocalDate.now());
    }

        private static final String BASE_URL = "/api/rewards/calculate/range/";

        @Test
        void testStartDateBeforeTwentyYearsAgo() throws Exception {
            String id = "1";
            String startDate = "1900-01-01"; // Date before 20 years
            String endDate = LocalDate.now().toString(); // Current date

            mockMvc.perform(get(BASE_URL + id)
                            .param("startDate", startDate)
                            .param("endDate", endDate))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidInputException))
                    .andExpect(result -> assertEquals(
                            "Start date cannot be earlier than " + LocalDate.now().minusYears(20),
                            result.getResolvedException().getMessage()
                    ));
        }

        @Test
        void testDatesInFuture() throws Exception {
            String id = "1";
            String startDate = LocalDate.now().plusDays(1).toString(); // Tomorrow's date
            String endDate = LocalDate.now().plusDays(2).toString();   // Day after tomorrow

            mockMvc.perform(get(BASE_URL + id)
                            .param("startDate", startDate)
                            .param("endDate", endDate))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidInputException))
                    .andExpect(result -> assertEquals(
                            "Dates cannot be in the future",
                            result.getResolvedException().getMessage()
                    ));
        }

        @Test
        void testEndDateBeforeStartDate() throws Exception {
            String id = "1";
            String startDate = LocalDate.now().toString(); // Current date
            String endDate = LocalDate.now().minusDays(1).toString(); // Yesterday's date

            mockMvc.perform(get(BASE_URL + id)
                            .param("startDate", startDate)
                            .param("endDate", endDate))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidInputException))
                    .andExpect(result -> assertEquals(
                            "End date cannot be before start date",
                            result.getResolvedException().getMessage()
                    ));
        }

        @Test
        void testInvalidId() throws Exception {
            String id = "0"; // Invalid ID
            String startDate = LocalDate.now().minusMonths(1).toString();
            String endDate = LocalDate.now().toString();

            mockMvc.perform(get(BASE_URL + id)
                            .param("startDate", startDate)
                            .param("endDate", endDate))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidInputException))
                    .andExpect(result -> assertEquals(
                            "ID cannot be null or zero",
                            result.getResolvedException().getMessage()
                    ));
        }


            // Positive test case for getTotalRewards
    @Test
    void getTotalRewards_success() throws Exception {
        // stub
        when(rewardService.getAllRewards(customerId)).thenReturn(loyaltyRewardResponse);

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/all/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loyaltyRewardResponse)));

        verify(rewardService, times(1)).getAllRewards(customerId);
    }

    // Negative test case for getTotalRewards - Exception scenario
    @Test
    void getTotalRewards_exception() throws Exception {
        // Arrange
        when(rewardService.getAllRewards(customerId)).thenThrow(new RewardsCalculationException("Unable to calculate total rewards",new RuntimeException()));

        // Act & Assert
        mockMvc.perform(get("/api/rewards/calculate/all/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unable to calculate total rewards"));

        verify(rewardService, times(1)).getAllRewards(customerId);
    }
}
