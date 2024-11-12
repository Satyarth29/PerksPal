package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.Reward;
import com.infy.retail.perkspal.respository.RewardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RewardServiceTest {
    @Mock
    private RewardRepository rewardRepository;
    @InjectMocks
    private RewardService rewardService;
    private List<Reward> rewardList;
    private final Long customerId = 1L;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Long customerId1 = 1L;
        customer = new Customer();
        customer.setName("satyarth");
        customer.setId(customerId1);
        rewardList = Arrays.asList(
                new Reward(customerId1, 120, LocalDate.of(2024, Month.JANUARY, 10), customer),
                new Reward(customerId1, 140, LocalDate.of(2024, Month.JANUARY, 12), customer),
                new Reward(customerId1, 63, LocalDate.of(2024, Month.FEBRUARY, 15), customer)
        );
    }

    // Positive test case for getRewardsPerMonth
    @Test
    void getRewardsPerMonth_success() throws PerksPalException {
        // Arrange

        when(rewardRepository.findAllByCustomerId(customerId)).thenReturn(rewardList);

        // Expected result
        Map<Month, Integer> expectedRewards = new HashMap<>();
        expectedRewards.put(Month.JANUARY, 260);
        expectedRewards.put(Month.FEBRUARY, 63);

        // Act
        Map<Month, Integer> rewardsPerMonth = rewardService.getRewardsPerMonth(customerId);

        // Assert
        assertEquals(expectedRewards, rewardsPerMonth);
        verify(rewardRepository, times(1)).findAllByCustomerId(customerId);
    }

    // Negative test case for getRewardsPerMonth - Exception Scenario
    @Test
    void getRewardsPerMonth_exception() {
        // Arrange

        when(rewardRepository.findAllByCustomerId(customerId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        PerksPalException exception = assertThrows(PerksPalException.class, () ->
                rewardService.getRewardsPerMonth(customerId)
        );

        assertEquals("Database error", exception.getMessage());
        verify(rewardRepository, times(1)).findAllByCustomerId(customerId);
    }

    // Positive test case for getAllRewards
    @Test
    void getAllRewards_success() throws PerksPalException {
        // Arrange

        when(rewardRepository.findAllByCustomerId(customerId)).thenReturn(rewardList);

        // Act
        Integer totalRewards = rewardService.getAllRewards(customerId);

        // Assert
        assertEquals(323, totalRewards);
        verify(rewardRepository, times(1)).findAllByCustomerId(customerId);
    }

    // Negative test case for getAllRewards - Exception Scenario
    @Test
    void getAllRewards_exception() {
        // Arrange
        when(rewardRepository.findAllByCustomerId(customerId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        PerksPalException exception = assertThrows(PerksPalException.class, () ->
                rewardService.getAllRewards(customerId)
        );

        assertEquals("Database error", exception.getMessage());
        verify(rewardRepository, times(1)).findAllByCustomerId(customerId);
    }

    // Positive test case for getRewardsList
    @Test
    void getRewardsList_success() throws PerksPalException {
        // Arrange

        when(rewardRepository.findAllByCustomerId(customerId)).thenReturn(rewardList);

        // Act
        List<Reward> result = rewardService.getRewardsList(customerId);

        // Assert
        assertEquals(3, result.size());
        assertEquals(rewardList, result);
        verify(rewardRepository, times(1)).findAllByCustomerId(customerId);
    }

    // Positive test case for saveReward
    @Test
    void saveReward_success() {
        // Arrange
        Reward reward = new Reward(1L, 110, LocalDate.of(2024, Month.JANUARY, 10), customer);

        // Act
        rewardService.saveReward(reward);

        // Assert
        verify(rewardRepository, times(1)).save(reward);
    }
}
