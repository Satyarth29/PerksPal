package com.infy.retail.perkspal.dto;

import java.time.LocalDate;

public record CustomerRequestDTO(
        String name,
        Double price,
        LocalDate date
) {
}
