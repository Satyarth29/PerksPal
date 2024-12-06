package com.infy.retail.perkspal.dto;

import java.util.Map;

public record LoyaltyRewardResponse(
        String name
        , Map<String,Integer> rewardPoints) {
}
