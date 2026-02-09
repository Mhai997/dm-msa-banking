package com.pichincha.dm.msa.banking.service.mapper;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.service.dto.AccountRequestDto;
import com.pichincha.dm.msa.banking.service.dto.AccountResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "client", ignore = true)      // se setea en el Service
    @Mapping(target = "movements", ignore = true)
    Account toEntity(AccountRequestDto dto);

    @Mapping(target = "clientId", source = "client.clientId")
    AccountResponseDto toResponse(Account entity);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "movements", ignore = true)
    void updateEntity(AccountRequestDto dto, @MappingTarget Account entity);
}
