package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.dto.TransactionPayload;
import com.infy.retail.perkspal.exceptions.InvalidInputException;
import com.infy.retail.perkspal.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/transaction")
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    /**
     * Handles the creation of a transaction based on the provided transaction payload.*
     * @param transactionPayload the payload containing transaction details such as
     *                          id and  price
     * @return a  ResponseEntity containing a success message if the transaction
     *         is processed successfully.
     * @throws InvalidInputException if the payload is invalid, e.g., missing or null
     *                                id, price.
     *                                     during processing.
     *  */
    @PostMapping("/create")
    public ResponseEntity<String> commitTransaction(@RequestBody TransactionPayload transactionPayload)  {
          log.info("transaction commit started with : {}", transactionPayload);
           if (ObjectUtils.isEmpty(transactionPayload.price()) || ObjectUtils.isEmpty(transactionPayload.id()))
               throw new InvalidInputException("please provide valid ID or price ");
           transactionService.saveTransaction(transactionPayload);
           return ResponseEntity.ok("Transaction completed successfully");
    }
}
