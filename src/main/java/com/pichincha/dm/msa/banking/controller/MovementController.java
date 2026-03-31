package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.service.MovementService;
import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService movementService;

    @PostMapping
    public ResponseEntity<MovementResponseDto> create(@Valid @RequestBody MovementRequestDto request) {
        MovementResponseDto response = movementService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{movementId}")
    public ResponseEntity<MovementResponseDto> getById(@PathVariable Long movementId) {
        return ResponseEntity.ok(movementService.getById(movementId));
    }

    @GetMapping
    public ResponseEntity<List<MovementResponseDto>> getAll() {
        return ResponseEntity.ok(movementService.findAllMovements());
    }

    @DeleteMapping("/{movementId}")
    public ResponseEntity<Void> delete(@PathVariable Long movementId) {
        movementService.delete(movementId);
        return ResponseEntity.noContent().build();
    }
}