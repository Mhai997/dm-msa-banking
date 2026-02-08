package com.pichincha.dm.msa.banking.repository;

import com.pichincha.dm.msa.banking.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByCustomerId(String customerId);

}