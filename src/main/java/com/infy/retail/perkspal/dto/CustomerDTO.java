package com.infy.retail.perkspal.dto;

import java.time.LocalDate;

public record CustomerDTO(
        String name,
        Double price,
        LocalDate date
) {
}
