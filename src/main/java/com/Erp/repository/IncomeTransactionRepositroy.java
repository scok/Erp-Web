package com.Erp.repository;

import com.Erp.entity.IncomeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeTransactionRepositroy extends JpaRepository<IncomeTransaction, Long> {
}
