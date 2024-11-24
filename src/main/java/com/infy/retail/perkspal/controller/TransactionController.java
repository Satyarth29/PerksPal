package com.infy.retail.perkspal.controller;

import com.infy.retail.perkspal.dto.CustomerRequestDTO;
import com.infy.retail.perkspal.exceptions.PerksPalException;
import com.infy.retail.perkspal.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger transactionalControllerLogger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping("/create")
    public ResponseEntity<String> commitTransaction(@RequestBody CustomerRequestDTO customerRequestDTO) throws PerksPalException {
        transactionalControllerLogger.info("TransactionController.commitTransaction starts with CustomerJson :{}",customerRequestDTO);
        try {
            if (ObjectUtils.isEmpty(customerRequestDTO.price()))
                throw new PerksPalException("please buy to proceed!!");
            transactionService.saveTransaction(customerRequestDTO);
            return ResponseEntity.ok("Transaction completed successfully");
        } catch (PerksPalException e) {
            throw new PerksPalException(e.getMessage(),e.getCause());
        }
    }
}
