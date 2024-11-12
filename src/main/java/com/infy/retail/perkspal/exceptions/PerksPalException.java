package com.infy.retail.perkspal.exceptions;

import org.springframework.http.HttpStatus;

public class PerksPalException extends Exception{
        public PerksPalException(String message) {
            super(message);
        }
        public PerksPalException(String message, Throwable cause) {
            super(message, cause);
        }
    }

