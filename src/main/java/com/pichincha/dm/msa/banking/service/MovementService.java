package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;

import java.util.List;

public interface MovementService {
    MovementResponseDto create(MovementRequestDto dto);
    MovementResponseDto getById(Long accountNumber);
    void delete(Long movementId);
    List<MovementResponseDto> findAllMovements();
}
