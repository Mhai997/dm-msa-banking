package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.ClientRepository;
import com.pichincha.dm.msa.banking.service.ClientService;
import com.pichincha.dm.msa.banking.service.dto.ClientRequestDto;
import com.pichincha.dm.msa.banking.service.dto.ClientResponseDto;
import com.pichincha.dm.msa.banking.service.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDto create(ClientRequestDto dto) {
        Client entity = clientMapper.toEntity(dto);
        return clientMapper.toResponse(clientRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getById(Long clientId) {
        Client entity = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        return clientMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> getAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Override
    public ClientResponseDto update(Long clientId, ClientRequestDto dto) {
        Client entity = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        clientMapper.updateEntity(dto, entity);
        return clientMapper.toResponse(clientRepository.save(entity));
    }

    @Override
    public void delete(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException("Client not found");
        }
        clientRepository.deleteById(clientId);
    }
}
