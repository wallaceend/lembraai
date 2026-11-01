package com.lembraai.dto;

import java.time.LocalDateTime;

public record PainelPacienteDTO(
        Long pacienteId,
        String nomePaciente,
        long pendentesHoje,
        long atrasadosHoje,
        long tomadosHoje,
        LocalDateTime proximoLembrete
) {}