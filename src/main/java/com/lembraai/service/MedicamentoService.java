package com.lembraai.service;

import com.lembraai.dto.MedicamentoRequestDTO;
import com.lembraai.dto.MedicamentoResponseDTO;
import com.lembraai.exception.RecursoNaoEncontradoException;
import com.lembraai.exception.RegraNegocioException;
import com.lembraai.model.Medicamento;
import com.lembraai.model.Usuario;
import com.lembraai.model.enums.Perfil;
import com.lembraai.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioService usuarioService;
    private final VinculoService vinculoService;
    private final LembreteService lembreteService;

    @Transactional
    public MedicamentoResponseDTO criar(Long solicitanteId, MedicamentoRequestDTO dto) {
        Usuario paciente = usuarioService.buscarPorId(dto.pacienteId());

        if (paciente.getPerfil() != Perfil.PACIENTE) {
            throw new RegraNegocioException("O usuário informado não possui perfil PACIENTE");
        }
        vinculoService.validarAcessoAoPaciente(solicitanteId, paciente.getId());
        validarDatas(dto);

        Medicamento medicamento = Medicamento.builder()
                .nome(dto.nome().trim())
                .dosagem(dto.dosagem())
                .frequenciaHoras(dto.frequenciaHoras())
                .horarioInicial(dto.horarioInicial())
                .dataInicio(dto.dataInicio())
                .dataFim(dto.dataFim())
                .observacoes(dto.observacoes())
                .paciente(paciente)
                .build();

        medicamento = medicamentoRepository.save(medicamento);
        lembreteService.gerarLembretes(medicamento);

        return MedicamentoResponseDTO.from(medicamento);
    }

    public List<MedicamentoResponseDTO> listarPorPaciente(Long pacienteId, Long solicitanteId) {
        vinculoService.validarAcessoAoPaciente(solicitanteId, pacienteId);
        return medicamentoRepository.findByPacienteId(pacienteId).stream()
                .map(MedicamentoResponseDTO::from)
                .toList();
    }

    public MedicamentoResponseDTO buscar(Long id, Long solicitanteId) {
        Medicamento medicamento = buscarEntidade(id);
        vinculoService.validarAcessoAoPaciente(solicitanteId, medicamento.getPaciente().getId());
        return MedicamentoResponseDTO.from(medicamento);
    }

    @Transactional
    public MedicamentoResponseDTO atualizar(Long id, Long solicitanteId, MedicamentoRequestDTO dto) {
        Medicamento medicamento = buscarEntidade(id);
        vinculoService.validarAcessoAoPaciente(solicitanteId, medicamento.getPaciente().getId());
        validarDatas(dto);

        // o paciente do medicamento não muda na atualização
        medicamento.setNome(dto.nome().trim());
        medicamento.setDosagem(dto.dosagem());
        medicamento.setFrequenciaHoras(dto.frequenciaHoras());
        medicamento.setHorarioInicial(dto.horarioInicial());
        medicamento.setDataInicio(dto.dataInicio());
        medicamento.setDataFim(dto.dataFim());
        medicamento.setObservacoes(dto.observacoes());

        medicamento = medicamentoRepository.save(medicamento);
        lembreteService.recriarPendentes(medicamento);

        return MedicamentoResponseDTO.from(medicamento);
    }

    @Transactional
    public void deletar(Long id, Long solicitanteId) {
        Medicamento medicamento = buscarEntidade(id);
        vinculoService.validarAcessoAoPaciente(solicitanteId, medicamento.getPaciente().getId());

        lembreteService.removerPorMedicamento(id);
        medicamentoRepository.delete(medicamento);
    }

    private Medicamento buscarEntidade(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Medicamento não encontrado: " + id));
    }

    private void validarDatas(MedicamentoRequestDTO dto) {
        if (dto.dataFim() != null && dto.dataFim().isBefore(dto.dataInicio())) {
            throw new RegraNegocioException("A data final não pode ser anterior à data de início");
        }
    }
}