package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.AccountRepository;
import com.pichincha.dm.msa.banking.repository.ClientRepository;
import com.pichincha.dm.msa.banking.service.AccountService;
import com.pichincha.dm.msa.banking.service.dto.AccountRequestDto;
import com.pichincha.dm.msa.banking.service.dto.AccountResponseDto;
import com.pichincha.dm.msa.banking.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseDto create(AccountRequestDto dto) {
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Account account = accountMapper.toEntity(dto);
        account.setClient(client);

        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto getById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getByClientId(Long clientId) {
        return accountRepository.findByClientClientId(clientId).stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    public AccountResponseDto update(Long accountId, AccountRequestDto dto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        accountMapper.updateEntity(dto, account);
        account.setClient(client);

        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public void delete(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new NotFoundException("Account not found");
        }
        accountRepository.deleteById(accountId);
    }
}
