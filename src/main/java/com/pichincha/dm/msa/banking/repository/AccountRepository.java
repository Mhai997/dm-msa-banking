package com.pichincha.dm.msa.banking.repository;

import com.pichincha.dm.msa.banking.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

  List<Account> findByCustomerId(Long customerId);

}
