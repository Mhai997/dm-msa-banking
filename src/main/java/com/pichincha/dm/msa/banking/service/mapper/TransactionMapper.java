package com.pichincha.dm.msa.banking.service.mapper;

import com.pichincha.dm.msa.banking.domain.Transaction;
import com.pichincha.dm.msa.banking.service.dto.TransactionCreateRequestDto;
import com.pichincha.dm.msa.banking.service.dto.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface TransactionMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "date", ignore = true)
  @Mapping(target = "balance", ignore = true)
  @Mapping(target = "account", ignore = true)
  Transaction toEntity (TransactionCreateRequestDto transactionCreateRequestDto);

  @Mapping(source = "account.id", target = "accountId")
  TransactionResponseDto toDto(Transaction tx);
}

