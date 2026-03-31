package com.pichincha.dm.msa.banking.service.mapper;

import com.pichincha.dm.msa.banking.domain.Movement;
import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mapping(target = "movementId", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "movementDate", ignore = true)
    Movement toEntity(MovementRequestDto dto);

    @Mapping(target = "accountId", source = "account.accountId")
    MovementResponseDto toResponse(Movement entity);
}