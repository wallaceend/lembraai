package com.lembraai.dto;

import com.lembraai.model.Lembrete;
import com.lembraai.model.enums.StatusLembrete;

import java.time.LocalDateTime;

public record LembreteResponseDTO(
        Long id,
        Long medicamentoId,
        String medicamentoNome,
        String dosagem,
        LocalDateTime horarioPrevisto,
        LocalDateTime horarioConfirmacao,
        StatusLembrete status
) {
    public static LembreteResponseDTO from(Lembrete l) {
        return new LembreteResponseDTO(
                l.getId(),
                l.getMedicamento().getId(),
                l.getMedicamento().getNome(),
                l.getMedicamento().getDosagem(),
                l.getHorarioPrevisto(),
                l.getHorarioConfirmacao(),
                l.getStatus());
    }
}