package com.infy.retail.perkspal.exceptions;

public class RewardsCalculationException extends RuntimeException{

    public RewardsCalculationException(String message,Exception exception){
        super(message,exception);
    }
}
