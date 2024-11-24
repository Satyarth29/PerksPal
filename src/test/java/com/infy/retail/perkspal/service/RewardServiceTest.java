package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerResponseDTO;
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
    void getRewardsInRangePerMonth_success() throws PerksPalException {
        // Arrange
        Map<String,Integer> totalRewardsMap = new HashMap<>();
        totalRewardsMap.put(LocalDate.of(2024, Month.FEBRUARY, 15).toString(),63);
        totalRewardsMap.put(LocalDate.of(2024, Month.JANUARY, 12).toString(),140);
        totalRewardsMap.put(LocalDate.of(2024, Month.JANUARY, 10).toString(),120);
        when(rewardRepository.findAllByCustomerIdAndDateBetween(any(),any(),any())).thenReturn(rewardList);

        // Act
        CustomerResponseDTO customerResponse = rewardService.getRewardsInRange(customerId,LocalDate.of(2024,01,10),LocalDate.of(2024,02,15));

        // Assert
        assertEquals(new CustomerResponseDTO("satyarth", totalRewardsMap), customerResponse);
        verify(rewardRepository, times(1)).findAllByCustomerIdAndDateBetween(customerId,LocalDate.of(2024,01,10),LocalDate.of(2024,02,15));
    }

    // Negative test case for getRewardsPerMonth - Exception Scenario
    @Test
    void getRewardsInRangePerMonth_exception() {
        // Arrange

        when(rewardRepository.findAllByCustomerIdAndDateBetween(customerId,LocalDate.of(2024,8,20),LocalDate.of(2024,11,20))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        PerksPalException exception = assertThrows(PerksPalException.class, () ->
                rewardService.getRewardsInRange(customerId,LocalDate.of(2024,8,20),LocalDate.of(2024,11,20))
        );

        assertEquals("Database error", exception.getMessage());
        verify(rewardRepository, times(1)).findAllByCustomerIdAndDateBetween(customerId,LocalDate.of(2024,8,20),LocalDate.of(2024,11,20));
    }

    // Positive test case for getAllRewards
    @Test
    void getAllRewards_success() throws PerksPalException {
        // Arrange

        when(rewardRepository.findAllByCustomerId(customerId)).thenReturn(rewardList);
        Map<String,Integer> totalRewardsMap = new HashMap<>();
        totalRewardsMap.put("total Rewards : ",323);
        // Act
        CustomerResponseDTO totalRewards = rewardService.getAllRewards(customerId);
        CustomerResponseDTO expectedCusResDTO = new CustomerResponseDTO("satyarth", totalRewardsMap);
        // Assert
        assertEquals(expectedCusResDTO, totalRewards);
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

        assertEquals("Error Processing", exception.getMessage());
        verify(rewardRepository, times(1)).findAllByCustomerId(customerId);
    }

    // Positive test case for getRewardsList
    @Test
    void getRewardsInRangeList_success() throws PerksPalException {
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
    void saveAllRewards_success() {
        // Arrange
        Reward reward = new Reward(1L, 110, LocalDate.of(2024, Month.JANUARY, 10), customer);

        // Act
        rewardService.saveAllRewards(List.of(reward));

        // Assert
        verify(rewardRepository, times(1)).saveAll(List.of(reward));
    }
}
