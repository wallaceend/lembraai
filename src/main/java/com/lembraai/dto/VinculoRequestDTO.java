package com.lembraai.dto;

import jakarta.validation.constraints.NotNull;

public record VinculoRequestDTO(
        @NotNull(message = "O cuidadorId é obrigatório") Long cuidadorId,
        @NotNull(message = "O pacienteId é obrigatório") Long pacienteId
) {}