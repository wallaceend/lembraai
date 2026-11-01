package com.lembraai.dto;

import java.time.LocalDateTime;

public record RelatorioSemanalDTO(
        Long pacienteId,
        String nomePaciente,
        LocalDateTime inicio,
        LocalDateTime fim,
        long totalDoses,
        long tomadas,
        long atrasadas,
        long pendentes,
        double percentualAdesao
) {}