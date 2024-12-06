package com.infy.retail.perkspal.exceptionhandler;

import com.infy.retail.perkspal.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + ex.getMessage());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.getMessage());
    }
    @ExceptionHandler(value ={InvalidInputException.class,RewardsCalculationException.class,NullPointerException.class} )
    public ResponseEntity<String> handleCustomRuntimeExceptions(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
    @ExceptionHandler(TransactionFailedException.class)
        public ResponseEntity<String> handleTransactionFailedException(TransactionFailedException transactionFailedException){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(transactionFailedException.getMessage());
    }
}
