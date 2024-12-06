package com.infy.retail.perkspal.exceptions;

public class TransactionFailedException extends RuntimeException {
    public TransactionFailedException(String message,Exception exception){
        super(message,exception);
    }
}
