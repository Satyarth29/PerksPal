package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.dto.CustomerDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> commitTransaction(@RequestBody  CustomerDTO customerDTO) throws PerksPalException {
        try {
            if (ObjectUtils.isEmpty(customerDTO.price()))
                throw new PerksPalException("please buy to proceed!!");

            transactionService.saveTransaction(customerDTO);
            return ResponseEntity.ok("Transaction completed successfully");
        } catch (PerksPalException e) {
            throw new PerksPalException(e.getMessage(),e.getCause());
        }
    }

}
