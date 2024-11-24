package com.infy.retail.perkspal.dto;

import java.util.Map;

public record CustomerResponseDTO(
        String name
        , Map<String,Integer> rewardPointsByDate) {
}
