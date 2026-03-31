package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.Gender;

import jakarta.validation.constraints.*;

public record ClientRequestDto(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 18, message = "El cliente debe ser mayor de edad")
        Integer age,

        @NotBlank(message = "La identificación es obligatoria")
        String identification,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe contener solo números")
        @Size(min = 10, max = 10, message = "El teléfono debe tener 10 dígitos")
        String phone,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        @NotNull(message = "El estado es obligatorio")
        Boolean status

) {}