package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;

public interface MovementService {
    MovementResponseDto create(MovementRequestDto dto);
    MovementResponseDto getById(Long movementId);
    void delete(Long movementId);
}
