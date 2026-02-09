package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.service.MovementService;
import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService movementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementResponseDto create(@RequestBody MovementRequestDto dto) {
        return movementService.create(dto);
    }

    @GetMapping("/{movementId}")
    public MovementResponseDto getById(@PathVariable Long movementId) {
        return movementService.getById(movementId);
    }

    @DeleteMapping("/{movementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movementId) {
        movementService.delete(movementId);
    }

    @GetMapping
    public List<MovementResponseDto> getAllMovements() {
        return movementService.findAllMovements();
    }
}
