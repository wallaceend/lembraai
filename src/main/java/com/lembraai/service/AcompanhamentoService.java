package com.lembraai.service;

import com.lembraai.dto.PainelPacienteDTO;
import com.lembraai.dto.RelatorioSemanalDTO;
import com.lembraai.exception.RegraNegocioException;
import com.lembraai.model.Lembrete;
import com.lembraai.model.Usuario;
import com.lembraai.model.enums.Perfil;
import com.lembraai.model.enums.StatusLembrete;
import com.lembraai.repository.LembreteRepository;
import com.lembraai.repository.VinculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcompanhamentoService {

    private final VinculoRepository vinculoRepository;
    private final LembreteRepository lembreteRepository;
    private final UsuarioService usuarioService;
    private final VinculoService vinculoService;

    // painel do cuidador (situação de hoje de cada paciente vinculado)
    public List<PainelPacienteDTO> painelDoCuidador(Long cuidadorId) {
        Usuario cuidador = usuarioService.buscarPorId(cuidadorId);
        if (cuidador.getPerfil() != Perfil.CUIDADOR) {
            throw new RegraNegocioException("Somente usuários com perfil CUIDADOR possuem painel");
        }

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fim = LocalDate.now().atTime(LocalTime.MAX);

        return vinculoRepository.findByCuidadorId(cuidadorId).stream().map(v -> {
            Usuario paciente = v.getPaciente();
            List<Lembrete> hoje = lembreteRepository
                    .findByMedicamentoPacienteIdAndHorarioPrevistoBetween(paciente.getId(), inicio, fim);

            LocalDateTime proximo = hoje.stream()
                    .filter(l -> l.getStatus() == StatusLembrete.PENDENTE)
                    .map(Lembrete::getHorarioPrevisto)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            return new PainelPacienteDTO(
                    paciente.getId(),
                    paciente.getNome(),
                    contar(hoje, StatusLembrete.PENDENTE),
                    contar(hoje, StatusLembrete.ATRASADO),
                    contar(hoje, StatusLembrete.TOMADO),
                    proximo);
        }).toList();
    }

    //  relatório de adesão dos últimos 7 dias
    public RelatorioSemanalDTO relatorioSemanal(Long pacienteId, Long solicitanteId) {
        vinculoService.validarAcessoAoPaciente(solicitanteId, pacienteId);
        Usuario paciente = usuarioService.buscarPorId(pacienteId);

        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusDays(7);

        List<Lembrete> lista = lembreteRepository
                .findByMedicamentoPacienteIdAndHorarioPrevistoBetween(pacienteId, inicio, fim);

        long total = lista.size();
        long tomadas = contar(lista, StatusLembrete.TOMADO);
        long atrasadas = contar(lista, StatusLembrete.ATRASADO);
        long pendentes = contar(lista, StatusLembrete.PENDENTE);
        double adesao = total == 0 ? 0.0 : Math.round(tomadas * 1000.0 / total) / 10.0;

        return new RelatorioSemanalDTO(pacienteId, paciente.getNome(), inicio, fim,
                total, tomadas, atrasadas, pendentes, adesao);
    }

    private long contar(List<Lembrete> lista, StatusLembrete status) {
        return lista.stream().filter(l -> l.getStatus() == status).count();
    }
}