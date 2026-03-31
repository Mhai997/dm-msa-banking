package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.service.ClientService;
import com.pichincha.dm.msa.banking.service.dto.ClientRequestDto;
import com.pichincha.dm.msa.banking.service.dto.ClientResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDto create(@Valid @RequestBody ClientRequestDto dto) {
        return clientService.create(dto);
    }

    @GetMapping("/{clientId}")
    public ClientResponseDto getById(@PathVariable Long clientId) {
        return clientService.getById(clientId);
    }

    @GetMapping
    public List<ClientResponseDto> getAll() {
        return clientService.getAll();
    }

    @PutMapping("/{clientId}")
    public ClientResponseDto update(@PathVariable Long clientId, @Valid @RequestBody ClientRequestDto dto) {
        return clientService.update(clientId, dto);
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long clientId) {
        clientService.delete(clientId);
    }
}
