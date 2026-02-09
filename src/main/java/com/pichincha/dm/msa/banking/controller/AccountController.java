package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.service.AccountService;
import com.pichincha.dm.msa.banking.service.dto.AccountRequestDto;
import com.pichincha.dm.msa.banking.service.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto create(@RequestBody AccountRequestDto dto) {
        return accountService.create(dto);
    }

    @GetMapping("/{accountId}")
    public AccountResponseDto getById(@PathVariable Long accountId) {
        return accountService.getById(accountId);
    }

    @GetMapping
    public List<AccountResponseDto> getAll() {
        return accountService.getAll();
    }

    // útil para reportes: cuentas por cliente
    @GetMapping("/by-client/{clientId}")
    public List<AccountResponseDto> getByClientId(@PathVariable Long clientId) {
        return accountService.getByClientId(clientId);
    }

    @PutMapping("/{accountId}")
    public AccountResponseDto update(@PathVariable Long accountId, @RequestBody AccountRequestDto dto) {
        return accountService.update(accountId, dto);
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long accountId) {
        accountService.delete(accountId);
    }
}
