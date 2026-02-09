package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;
import com.pichincha.dm.msa.banking.service.AccountService;
import com.pichincha.dm.msa.banking.service.dto.AccountRequestDto;
import com.pichincha.dm.msa.banking.service.dto.AccountResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountController Tests")
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        AccountController accountController = new AccountController(accountService);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @DisplayName("POST /cuentas - Crear una nueva cuenta exitosamente")
    void testCreateAccount_Success() throws Exception {
        // Arrange
        AccountResponseDto responseDto = new AccountResponseDto(
                1L,
                "1234567890",
                AccountType.SAVINGS,
                new BigDecimal("1000.00"),
                true,
                1L
        );

        when(accountService.create(any(AccountRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"initialBalance\":1000.00,\"status\":true,\"clientId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.initialBalance").value(1000.00))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.clientId").value(1));

        verify(accountService, times(1)).create(any(AccountRequestDto.class));
    }

    @Test
    @DisplayName("GET /cuentas/{accountId} - Obtener cuenta por ID exitosamente")
    void testGetById_Success() throws Exception {
        // Arrange
        Long accountId = 1L;
        AccountResponseDto responseDto = new AccountResponseDto(
                accountId,
                "1234567890",
                AccountType.SAVINGS,
                new BigDecimal("1000.00"),
                true,
                1L
        );

        when(accountService.getById(accountId)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/cuentas/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.initialBalance").value(1000.00));

        verify(accountService, times(1)).getById(accountId);
    }

    @Test
    @DisplayName("GET /cuentas - Obtener todas las cuentas exitosamente")
    void testGetAll_Success() throws Exception {
        // Arrange
        List<AccountResponseDto> accounts = Arrays.asList(
                new AccountResponseDto(1L, "1234567890", AccountType.SAVINGS, new BigDecimal("1000.00"), true, 1L),
                new AccountResponseDto(2L, "0987654321", AccountType.CHECKING, new BigDecimal("5000.00"), true, 2L)
        );

        when(accountService.getAll()).thenReturn(accounts);

        // Act & Assert
        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].accountNumber").value("1234567890"))
                .andExpect(jsonPath("$[1].accountId").value(2))
                .andExpect(jsonPath("$[1].accountNumber").value("0987654321"));

        verify(accountService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /cuentas/by-client/{clientId} - Obtener cuentas por cliente exitosamente")
    void testGetByClientId_Success() throws Exception {
        // Arrange
        Long clientId = 1L;
        List<AccountResponseDto> accounts = Arrays.asList(
                new AccountResponseDto(1L, "1234567890", AccountType.SAVINGS, new BigDecimal("1000.00"), true, clientId),
                new AccountResponseDto(2L, "1234567891", AccountType.CHECKING, new BigDecimal("2000.00"), true, clientId)
        );

        when(accountService.getByClientId(clientId)).thenReturn(accounts);

        // Act & Assert
        mockMvc.perform(get("/cuentas/by-client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientId").value(clientId))
                .andExpect(jsonPath("$[1].clientId").value(clientId));

        verify(accountService, times(1)).getByClientId(clientId);
    }

    @Test
    @DisplayName("PUT /cuentas/{accountId} - Actualizar cuenta exitosamente")
    void testUpdate_Success() throws Exception {
        // Arrange
        Long accountId = 1L;
        AccountResponseDto responseDto = new AccountResponseDto(
                accountId,
                "1234567890",
                AccountType.SAVINGS,
                new BigDecimal("5000.00"),
                true,
                1L
        );

        when(accountService.update(eq(accountId), any(AccountRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/cuentas/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"initialBalance\":5000.00,\"status\":true,\"clientId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.initialBalance").value(5000.00));

        verify(accountService, times(1)).update(eq(accountId), any(AccountRequestDto.class));
    }

    @Test
    @DisplayName("DELETE /cuentas/{accountId} - Eliminar cuenta exitosamente")
    void testDelete_Success() throws Exception {
        // Arrange
        Long accountId = 1L;
        doNothing().when(accountService).delete(accountId);

        // Act & Assert
        mockMvc.perform(delete("/cuentas/{accountId}", accountId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).delete(accountId);
    }

    @Test
    @DisplayName("GET /cuentas - Obtener lista vacía de cuentas")
    void testGetAll_EmptyList() throws Exception {
        // Arrange
        when(accountService.getAll()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(accountService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /cuentas/by-client/{clientId} - Cliente sin cuentas")
    void testGetByClientId_NoAccounts() throws Exception {
        // Arrange
        Long clientId = 99L;
        when(accountService.getByClientId(clientId)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/cuentas/by-client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(accountService, times(1)).getByClientId(clientId);
    }

}

