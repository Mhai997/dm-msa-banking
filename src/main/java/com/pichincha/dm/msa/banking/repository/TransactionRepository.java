package com.pichincha.dm.msa.banking.repository;

import com.pichincha.dm.msa.banking.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Optional<Transaction> findTopByAccountIdOrderByDateDesc(Long accountId);

}
