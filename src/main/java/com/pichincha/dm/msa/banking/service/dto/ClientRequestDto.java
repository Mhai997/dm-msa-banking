package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.Gender;

public record ClientRequestDto(
        String name,
        Gender gender,
        Integer age,
        String identification,
        String address,
        String phone,
        String password,
        Boolean status
) {}
