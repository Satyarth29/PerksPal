package com.infy.retail.perkspal.respository;

import com.infy.retail.perkspal.models.RetailTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetailTransactionRepository extends JpaRepository<RetailTransaction,Long> {
    List<RetailTransaction> findAllByCustomerId(Long CustomerID);
}
