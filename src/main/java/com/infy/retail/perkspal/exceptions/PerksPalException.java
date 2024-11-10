package com.infy.retail.perkspal.exceptions;

public class PerksPalException extends Exception{
    public PerksPalException(Long n ){
        super(String.format("%s not found with",n));
    }
}
