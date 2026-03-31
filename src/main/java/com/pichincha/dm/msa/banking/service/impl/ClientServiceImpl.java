package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.exception.BusinessException;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.ClientRepository;
import com.pichincha.dm.msa.banking.service.ClientService;
import com.pichincha.dm.msa.banking.service.dto.ClientRequestDto;
import com.pichincha.dm.msa.banking.service.dto.ClientResponseDto;
import com.pichincha.dm.msa.banking.service.mapper.ClientMapper;
import com.pichincha.dm.msa.banking.util.EcuadorIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final String CLIENT_NOT_FOUND = "Cliente no encontrado";
    private static final String INVALID_IDENTIFICATION = "La cédula no es válida";
    private static final String UNDERAGE_CLIENT = "El cliente debe ser mayor de edad";
    private static final String DUPLICATE_IDENTIFICATION = "Ya existe un cliente con esta identificación";
    private static final String INVALID_PHONE = "El número de teléfono no es válido";
    private static final String INVALID_PASSWORD = "La contraseña debe tener al menos 6 caracteres";

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDto create(ClientRequestDto request) {
        validateClientData(request);
        validateIdentificationUniqueness(request.identification());

        Client client = clientMapper.toEntity(request);
        Client savedClient = clientRepository.save(client);

        return clientMapper.toResponse(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getById(Long clientId) {
        Client client = findClientById(clientId);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Override
    public ClientResponseDto update(Long clientId, ClientRequestDto request) {
        Client existingClient = findClientById(clientId);

        validateClientData(request);
        validateIdentificationUniquenessForUpdate(clientId, request.identification());

        clientMapper.updateEntity(request, existingClient);
        Client updatedClient = clientRepository.save(existingClient);

        return clientMapper.toResponse(updatedClient);
    }

    @Override
    public void delete(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException(CLIENT_NOT_FOUND);
        }
        clientRepository.deleteById(clientId);
    }

    private Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(CLIENT_NOT_FOUND));
    }

    private void validateClientData(ClientRequestDto request) {
        validateIdentification(request.identification());
        validateAge(request.age());
        validatePhone(request.phone());
        validatePassword(request.password());
    }

    private void validateIdentification(String identification) {
        if (!EcuadorIdValidator.isValid(identification)) {
            throw new BusinessException(INVALID_IDENTIFICATION);
        }
    }

    private void validateAge(Integer age) {
        if (age < 18) {
            throw new BusinessException(UNDERAGE_CLIENT);
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.length() != 10) {
            throw new BusinessException(INVALID_PHONE);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new BusinessException(INVALID_PASSWORD);
        }
    }

    private void validateIdentificationUniqueness(String identification) {
        if (clientRepository.existsByIdentification(identification)) {
            throw new BusinessException(DUPLICATE_IDENTIFICATION);
        }
    }

    private void validateIdentificationUniquenessForUpdate(Long clientId, String identification) {
        clientRepository.findByIdentification(identification)
                .filter(client -> !client.getClientId().equals(clientId))
                .ifPresent(client -> {
                    throw new BusinessException(DUPLICATE_IDENTIFICATION);
                });
    }
}