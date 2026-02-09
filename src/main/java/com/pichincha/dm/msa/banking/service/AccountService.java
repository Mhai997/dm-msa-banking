package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.service.dto.AccountRequestDto;
import com.pichincha.dm.msa.banking.service.dto.AccountResponseDto;

import java.util.List;

public interface AccountService {
    AccountResponseDto create(AccountRequestDto dto);
    AccountResponseDto getById(Long accountId);
    List<AccountResponseDto> getAll();
    List<AccountResponseDto> getByClientId(Long clientId);
    AccountResponseDto update(Long accountId, AccountRequestDto dto);
    void delete(Long accountId);
}
