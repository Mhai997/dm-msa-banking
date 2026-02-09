package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.service.dto.ClientRequestDto;
import com.pichincha.dm.msa.banking.service.dto.ClientResponseDto;

import java.util.List;

public interface ClientService {
    ClientResponseDto create(ClientRequestDto dto);
    ClientResponseDto getById(Long clientId);
    List<ClientResponseDto> getAll();
    ClientResponseDto update(Long clientId, ClientRequestDto dto);
    void delete(Long clientId);
}
