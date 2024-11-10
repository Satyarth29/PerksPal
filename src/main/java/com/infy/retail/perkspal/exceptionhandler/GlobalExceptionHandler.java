package com.infy.retail.perkspal.exceptionhandler;

import com.infy.retail.perkspal.exceptions.PerksPalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PerksPalException.class)
    public ResponseEntity<String> responseEntity(PerksPalException ex){
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }
}
