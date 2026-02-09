package com.pichincha.dm.msa.banking.service.mapper;

import com.pichincha.dm.msa.banking.domain.Movement;
import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mapping(target = "movementId", ignore = true)
    @Mapping(target = "account", ignore = true) // se setea en el Service
    @Mapping(target = "balance", ignore = true) // se calcula en el Service
    Movement toEntity(MovementRequestDto dto);

    @Mapping(target = "accountId", source = "account.accountId")
    MovementResponseDto toResponse(Movement entity);
}
