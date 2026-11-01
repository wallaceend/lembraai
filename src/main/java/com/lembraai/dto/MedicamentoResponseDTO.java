package com.lembraai.dto;

import com.lembraai.model.Medicamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record MedicamentoResponseDTO(
        Long id,
        String nome,
        String dosagem,
        Integer frequenciaHoras,
        LocalTime horarioInicial,
        LocalDate dataInicio,
        LocalDate dataFim,
        String observacoes,
        Boolean ativo,
        Long pacienteId
) {
    public static MedicamentoResponseDTO from(Medicamento m) {
        return new MedicamentoResponseDTO(
                m.getId(), m.getNome(), m.getDosagem(), m.getFrequenciaHoras(),
                m.getHorarioInicial(), m.getDataInicio(), m.getDataFim(),
                m.getObservacoes(), m.getAtivo(), m.getPaciente().getId());
    }
}