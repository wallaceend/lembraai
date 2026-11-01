package com.lembraai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record MedicamentoRequestDTO(
        @NotBlank(message = "O nome do medicamento é obrigatório") String nome,
        String dosagem,
        @NotNull(message = "A frequência em horas é obrigatória")
        @Min(value = 1, message = "A frequência mínima é 1 hora")
        @Max(value = 168, message = "A frequência máxima é 168 horas") Integer frequenciaHoras,
        @NotNull(message = "O horário inicial é obrigatório (ex.: 08:00)") LocalTime horarioInicial,
        @NotNull(message = "A data de início é obrigatória (ex.: 2026-11-01)") LocalDate dataInicio,
        LocalDate dataFim,
        String observacoes,
        @NotNull(message = "O pacienteId é obrigatório") Long pacienteId
) {}