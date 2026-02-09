package com.pichincha.dm.msa.banking.repository;

import com.pichincha.dm.msa.banking.domain.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountAccountIdAndMovementDateBetween(
            Long accountId,
            LocalDateTime from,
            LocalDateTime to
    );
    Optional<Movement> findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(Long accountId);
    Optional<Movement> findTopByAccountAccountIdAndMovementDateLessThanOrderByMovementDateDescMovementIdDesc(
            Long accountId, LocalDateTime before);

}
