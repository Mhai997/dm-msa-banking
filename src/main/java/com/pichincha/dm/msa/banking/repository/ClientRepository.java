package com.pichincha.dm.msa.banking.repository;

import com.pichincha.dm.msa.banking.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByIdentification(String identification);
}
