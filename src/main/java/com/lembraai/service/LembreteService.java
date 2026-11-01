package com.lembraai.service;

import com.lembraai.dto.LembreteResponseDTO;
import com.lembraai.exception.AcessoNegadoException;
import com.lembraai.exception.RecursoNaoEncontradoException;
import com.lembraai.exception.RegraNegocioException;
import com.lembraai.model.Lembrete;
import com.lembraai.model.Medicamento;
import com.lembraai.model.enums.StatusLembrete;
import com.lembraai.repository.LembreteRepository;
import com.lembraai.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LembreteService {

    private final LembreteRepository lembreteRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final VinculoService vinculoService;

    @Value("${lembraai.limite-atraso-minutos:30}")
    private int limiteAtrasoMinutos;

    @Value("${lembraai.dias-geracao-lembretes:7}")
    private int diasGeracao;

    //  gera os lembretes futuros com base na frequência do medicamento
    @Transactional
    public void gerarLembretes(Medicamento m) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limite = agora.plusDays(diasGeracao);

        if (m.getDataFim() != null) {
            LocalDateTime fimTratamento = m.getDataFim().atTime(LocalTime.MAX);
            if (fimTratamento.isBefore(limite)) {
                limite = fimTratamento;
            }
        }

        LocalDateTime horario = m.getDataInicio().atTime(m.getHorarioInicial());
        while (horario.isBefore(agora)) {
            horario = horario.plusHours(m.getFrequenciaHoras());
        }

        List<Lembrete> novos = new ArrayList<>();
        while (!horario.isAfter(limite)) {
            if (!lembreteRepository.existsByMedicamentoIdAndHorarioPrevisto(m.getId(), horario)) {
                novos.add(Lembrete.builder().medicamento(m).horarioPrevisto(horario).build());
            }
            horario = horario.plusHours(m.getFrequenciaHoras());
        }
        lembreteRepository.saveAll(novos);
    }

    // usado quando o medicamento é editado: apaga os pendentes e gera de novo
    @Transactional
    public void recriarPendentes(Medicamento m) {
        lembreteRepository.deleteByMedicamentoIdAndStatus(m.getId(), StatusLembrete.PENDENTE);
        gerarLembretes(m);
    }

    @Transactional
    public void removerPorMedicamento(Long medicamentoId) {
        lembreteRepository.deleteByMedicamentoId(medicamentoId);
    }

    public List<LembreteResponseDTO> listarDoPaciente(Long pacienteId, Long solicitanteId, StatusLembrete status) {
        vinculoService.validarAcessoAoPaciente(solicitanteId, pacienteId);

        List<Lembrete> lista = (status == null)
                ? lembreteRepository.findByMedicamentoPacienteId(pacienteId)
                : lembreteRepository.findByMedicamentoPacienteIdAndStatus(pacienteId, status);

        return lista.stream()
                .sorted(Comparator.comparing(Lembrete::getHorarioPrevisto))
                .map(LembreteResponseDTO::from)
                .toList();
    }

    //  ação "Tomei"
    @Transactional
    public LembreteResponseDTO confirmarTomei(Long lembreteId, Long solicitanteId) {
        Lembrete lembrete = lembreteRepository.findById(lembreteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lembrete não encontrado: " + lembreteId));

        Long pacienteId = lembrete.getMedicamento().getPaciente().getId();
        if (!pacienteId.equals(solicitanteId)) {
            throw new AcessoNegadoException("Somente o paciente pode confirmar a dose");
        }
        if (lembrete.getStatus() == StatusLembrete.TOMADO) {
            throw new RegraNegocioException("Essa dose já foi confirmada");
        }

        lembrete.setStatus(StatusLembrete.TOMADO);
        lembrete.setHorarioConfirmacao(LocalDateTime.now());
        return LembreteResponseDTO.from(lembreteRepository.save(lembrete));
    }

    // PENDENTE -> ATRASADO quando passa do limite (roda a cada 1 minuto)
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void marcarAtrasados() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(limiteAtrasoMinutos);
        List<Lembrete> atrasados = lembreteRepository
                .findByStatusAndHorarioPrevistoBefore(StatusLembrete.PENDENTE, limite);

        atrasados.forEach(l -> l.setStatus(StatusLembrete.ATRASADO));
        lembreteRepository.saveAll(atrasados);
    }

    // todo dia às 00:05 completa a janela de lembretes dos medicamentos ativos
    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void gerarLembretesDiarios() {
        medicamentoRepository.findByAtivoTrue().forEach(this::gerarLembretes);
    }
}