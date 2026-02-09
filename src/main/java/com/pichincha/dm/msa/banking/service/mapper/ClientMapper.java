package com.pichincha.dm.msa.banking.service.mapper;

import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.service.dto.ClientRequestDto;
import com.pichincha.dm.msa.banking.service.dto.ClientResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    Client toEntity(ClientRequestDto dto);

    ClientResponseDto toResponse(Client entity);

    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    void updateEntity(ClientRequestDto dto, @MappingTarget Client entity);
}
