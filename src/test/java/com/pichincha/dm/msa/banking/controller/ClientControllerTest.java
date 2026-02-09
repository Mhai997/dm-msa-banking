package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.domain.enums.Gender;
import com.pichincha.dm.msa.banking.service.ClientService;
import com.pichincha.dm.msa.banking.service.dto.ClientRequestDto;
import com.pichincha.dm.msa.banking.service.dto.ClientResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientController Tests")
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        ClientController clientController = new ClientController(clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    @DisplayName("POST /clientes - Crear un nuevo cliente exitosamente")
    void testCreateClient_Success() throws Exception {
        // Arrange
        ClientResponseDto responseDto = new ClientResponseDto(
                1L,
                "Juan Pérez",
                Gender.MALE,
                30,
                "1234567890",
                "Calle Principal 123",
                "0987654321",
                true
        );

        when(clientService.create(any(ClientRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Juan Pérez\",\"gender\":\"MALE\",\"age\":30,\"identification\":\"1234567890\",\"address\":\"Calle Principal 123\",\"phone\":\"0987654321\",\"password\":\"password123\",\"status\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.address").value("Calle Principal 123"))
                .andExpect(jsonPath("$.phone").value("0987654321"))
                .andExpect(jsonPath("$.status").value(true));

        verify(clientService, times(1)).create(any(ClientRequestDto.class));
    }

    @Test
    @DisplayName("GET /clientes/{clientId} - Obtener cliente por ID exitosamente")
    void testGetById_Success() throws Exception {
        // Arrange
        Long clientId = 1L;
        ClientResponseDto responseDto = new ClientResponseDto(
                clientId,
                "Juan Pérez",
                Gender.MALE,
                30,
                "1234567890",
                "Calle Principal 123",
                "0987654321",
                true
        );

        when(clientService.getById(clientId)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/clientes/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.identification").value("1234567890"));

        verify(clientService, times(1)).getById(clientId);
    }

    @Test
    @DisplayName("GET /clientes - Obtener todos los clientes exitosamente")
    void testGetAll_Success() throws Exception {
        // Arrange
        List<ClientResponseDto> clients = Arrays.asList(
                new ClientResponseDto(1L, "Juan Pérez", Gender.MALE, 30, "1234567890", "Calle Principal 123", "0987654321", true),
                new ClientResponseDto(2L, "María García", Gender.FEMALE, 28, "0987654321", "Calle Secundaria 456", "1234567890", true)
        );

        when(clientService.getAll()).thenReturn(clients);

        // Act & Assert
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].clientId").value(2))
                .andExpect(jsonPath("$[1].name").value("María García"));

        verify(clientService, times(1)).getAll();
    }

    @Test
    @DisplayName("PUT /clientes/{clientId} - Actualizar cliente exitosamente")
    void testUpdate_Success() throws Exception {
        // Arrange
        Long clientId = 1L;
        ClientResponseDto responseDto = new ClientResponseDto(
                clientId,
                "Juan Pérez Actualizado",
                Gender.MALE,
                31,
                "1234567890",
                "Calle Principal 456",
                "0987654322",
                true
        );

        when(clientService.update(eq(clientId), any(ClientRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/clientes/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Juan Pérez Actualizado\",\"gender\":\"MALE\",\"age\":31,\"identification\":\"1234567890\",\"address\":\"Calle Principal 456\",\"phone\":\"0987654322\",\"password\":\"password123\",\"status\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId))
                .andExpect(jsonPath("$.name").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.age").value(31));

        verify(clientService, times(1)).update(eq(clientId), any(ClientRequestDto.class));
    }

    @Test
    @DisplayName("DELETE /clientes/{clientId} - Eliminar cliente exitosamente")
    void testDelete_Success() throws Exception {
        // Arrange
        Long clientId = 1L;
        doNothing().when(clientService).delete(clientId);

        // Act & Assert
        mockMvc.perform(delete("/clientes/{clientId}", clientId))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).delete(clientId);
    }

    @Test
    @DisplayName("GET /clientes - Obtener lista vacía de clientes")
    void testGetAll_EmptyList() throws Exception {
        // Arrange
        when(clientService.getAll()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(clientService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /clientes - Obtener múltiples clientes con diferentes géneros")
    void testGetAll_MultipleClientsWithDifferentGenders() throws Exception {
        // Arrange
        List<ClientResponseDto> clients = Arrays.asList(
                new ClientResponseDto(1L, "Juan Pérez", Gender.MALE, 30, "1234567890", "Calle Principal 123", "0987654321", true),
                new ClientResponseDto(2L, "María García", Gender.FEMALE, 28, "0987654321", "Calle Secundaria 456", "1234567890", true),
                new ClientResponseDto(3L, "Carlos López", Gender.MALE, 35, "1122334455", "Calle Terciaria 789", "5544332211", true)
        );

        when(clientService.getAll()).thenReturn(clients);

        // Act & Assert
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[1].gender").value("FEMALE"))
                .andExpect(jsonPath("$[2].gender").value("MALE"));

        verify(clientService, times(1)).getAll();
    }

    @Test
    @DisplayName("POST /clientes - Cliente mujer creado exitosamente")
    void testCreateClient_Female_Success() throws Exception {
        // Arrange
        ClientResponseDto responseDto = new ClientResponseDto(
                2L,
                "María García",
                Gender.FEMALE,
                28,
                "0987654321",
                "Calle Secundaria 456",
                "1234567890",
                true
        );

        when(clientService.create(any(ClientRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"María García\",\"gender\":\"FEMALE\",\"age\":28,\"identification\":\"0987654321\",\"address\":\"Calle Secundaria 456\",\"phone\":\"1234567890\",\"password\":\"password123\",\"status\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(2))
                .andExpect(jsonPath("$.name").value("María García"))
                .andExpect(jsonPath("$.gender").value("FEMALE"))
                .andExpect(jsonPath("$.status").value(true));

        verify(clientService, times(1)).create(any(ClientRequestDto.class));
    }

    @Test
    @DisplayName("PUT /clientes/{clientId} - Actualizar estado del cliente a inactivo")
    void testUpdate_StatusInactive() throws Exception {
        // Arrange
        Long clientId = 1L;
        ClientResponseDto responseDto = new ClientResponseDto(
                clientId,
                "Juan Pérez",
                Gender.MALE,
                30,
                "1234567890",
                "Calle Principal 123",
                "0987654321",
                false
        );

        when(clientService.update(eq(clientId), any(ClientRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/clientes/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Juan Pérez\",\"gender\":\"MALE\",\"age\":30,\"identification\":\"1234567890\",\"address\":\"Calle Principal 123\",\"phone\":\"0987654321\",\"password\":\"password123\",\"status\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId))
                .andExpect(jsonPath("$.status").value(false));

        verify(clientService, times(1)).update(eq(clientId), any(ClientRequestDto.class));
    }

}

