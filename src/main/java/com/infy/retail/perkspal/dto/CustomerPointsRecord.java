package com.infy.retail.perkspal.dto;

import com.infy.retail.perkspal.models.Customer;

import java.time.LocalDate;


public record CustomerPointsRecord(
       Customer customer,
        Integer point,
        LocalDate date) {

}

