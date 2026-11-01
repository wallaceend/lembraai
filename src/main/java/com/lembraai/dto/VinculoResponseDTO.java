package com.lembraai.dto;

import com.lembraai.model.Vinculo;

public record VinculoResponseDTO(Long id, Long cuidadorId, Long pacienteId) {

    public static VinculoResponseDTO from(Vinculo v) {
        return new VinculoResponseDTO(v.getId(), v.getCuidador().getId(), v.getPaciente().getId());
    }
}