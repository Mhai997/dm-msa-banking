package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.exception.BusinessException;
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

    private static final String ACCOUNT_NOT_FOUND = "Cuenta no encontrada";
    private static final String CLIENT_NOT_FOUND = "Cliente no encontrado";
    private static final String DUPLICATE_ACCOUNT_NUMBER = "Ya existe una cuenta con este número";

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseDto create(AccountRequestDto request) {
        validateAccountNumberUniqueness(request.accountNumber());

        Client client = findClientById(request.clientId());

        Account account = accountMapper.toEntity(request);
        account.setClient(client);

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto getById(Long accountId) {
        Account account = findAccountById(accountId);
        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getByClientId(Long clientId) {
        return accountRepository.findByClientClientId(clientId)
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    public AccountResponseDto update(Long accountId, AccountRequestDto request) {
        Account existingAccount = findAccountById(accountId);
        Client client = findClientById(request.clientId());

        validateAccountNumberUniquenessForUpdate(accountId, request.accountNumber());

        accountMapper.updateEntity(request, existingAccount);
        existingAccount.setClient(client);

        Account updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    public void delete(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new NotFoundException(ACCOUNT_NOT_FOUND);
        }
        accountRepository.deleteById(accountId);
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));
    }

    private Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(CLIENT_NOT_FOUND));
    }

    private void validateAccountNumberUniqueness(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new BusinessException(DUPLICATE_ACCOUNT_NUMBER);
        }
    }

    private void validateAccountNumberUniquenessForUpdate(Long accountId, String accountNumber) {
        accountRepository.findByAccountNumber(accountNumber)
                .filter(account -> !account.getAccountId().equals(accountId))
                .ifPresent(account -> {
                    throw new BusinessException(DUPLICATE_ACCOUNT_NUMBER);
                });
    }
}