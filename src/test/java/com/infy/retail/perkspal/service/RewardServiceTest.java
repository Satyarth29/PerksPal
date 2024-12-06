package com.infy.retail.perkspal.service;

import com.infy.retail.perkspal.dto.CustomerPointsRecord;
import com.infy.retail.perkspal.dto.LoyaltyRewardResponse;
import com.infy.retail.perkspal.models.Customer;
import com.infy.retail.perkspal.models.RetailTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RewardServiceTest {
  @Mock
  CustomerService customerService;
    @InjectMocks
    private RewardService rewardService;
    private List<CustomerPointsRecord> customerPointsRecordList;
    private final Long customerId = 1L;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Long customerId1 = 1L;
        customer = new Customer();
        customer.setName("satyarth");
        customer.setId(customerId1);
        customer.setRetailTransactions(List.of(new RetailTransaction(1L,customer,120.0,LocalDate.of(2024, Month.JANUARY, 10))
                                                , new RetailTransaction(2L,customer, 140.0, LocalDate.of(2024, Month.JANUARY, 12))));
        customerPointsRecordList = Arrays.asList(
                new CustomerPointsRecord(  customer,90, LocalDate.of(2024, Month.JANUARY, 10)),
                new CustomerPointsRecord(customer, 110, LocalDate.of(2024, Month.JANUARY, 12))
        );
    }

    // Positive test case for getRewardsPerMonth
    @Test
    void getRewardsInRangePerMonth_success() {
        // Arrange
        Map<String,Integer> totalRewardsMap = new HashMap<>();
        totalRewardsMap.put(LocalDate.of(2024, Month.JANUARY, 12).toString(),130);
        totalRewardsMap.put(LocalDate.of(2024, Month.JANUARY, 10).toString(),90);
        when(customerService.findById(any())).thenReturn(Optional.ofNullable(customer));

        // Act
        LoyaltyRewardResponse customerResponse = rewardService.getRewardsInRange(customerId,LocalDate.of(2024,01,10),LocalDate.of(2024,02,15));

        // Assert
        assertEquals(new LoyaltyRewardResponse("satyarth", totalRewardsMap), customerResponse);
        verify(customerService, times(1)).findById(customerId);
    }

    // Negative test case for getRewardsPerMonth - Exception Scenario
    @Test
    void getRewardsInRangePerMonth_exception() {
        // Arrange

        when(customerService.findById(customerId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                rewardService.getRewardsInRange(customerId,LocalDate.of(2024,8,20),LocalDate.of(2024,11,20))
        );

        assertEquals("Database error", exception.getMessage());
        verify(customerService, times(1)).findById(customerId);
    }

    // Positive test case for getAllRewards
    @Test
    void getAllRewards_success() {
        // Arrange

        when(customerService.findById(customerId)).thenReturn(Optional.ofNullable(customer));
        Map<String,Integer> totalRewardsMap = new HashMap<>();
        totalRewardsMap.put("Customers total Rewards : ",220);
        // Act
        LoyaltyRewardResponse totalRewards = rewardService.getAllRewards(customerId);
        LoyaltyRewardResponse expectedCusResDTO = new LoyaltyRewardResponse("satyarth", totalRewardsMap);
        // Assert
        assertEquals(expectedCusResDTO, totalRewards);
        verify(customerService, times(1)).findById(customerId);
    }

    // Negative test case for getAllRewards - Exception Scenario
    @Test
    void getAllRewards_exception() {
        // Arrange
        when(customerService.findById(customerId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                rewardService.getAllRewards(customerId)
        );

        assertEquals("Database error", exception.getMessage());
        verify(customerService, times(1)).findById(customerId);
    }
}
