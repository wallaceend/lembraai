package com.lembraai.service;

import com.lembraai.dto.UsuarioResponseDTO;
import com.lembraai.dto.VinculoRequestDTO;
import com.lembraai.dto.VinculoResponseDTO;
import com.lembraai.exception.AcessoNegadoException;
import com.lembraai.exception.RegraNegocioException;
import com.lembraai.model.Usuario;
import com.lembraai.model.Vinculo;
import com.lembraai.model.enums.Perfil;
import com.lembraai.repository.VinculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VinculoService {

    private final VinculoRepository vinculoRepository;
    private final UsuarioService usuarioService;

    public VinculoResponseDTO vincular(VinculoRequestDTO dto) {
        Usuario cuidador = usuarioService.buscarPorId(dto.cuidadorId());
        Usuario paciente = usuarioService.buscarPorId(dto.pacienteId());

        if (cuidador.getPerfil() != Perfil.CUIDADOR) {
            throw new RegraNegocioException("O usuário informado como cuidador não possui perfil CUIDADOR");
        }
        if (paciente.getPerfil() != Perfil.PACIENTE) {
            throw new RegraNegocioException("O usuário informado como paciente não possui perfil PACIENTE");
        }
        if (vinculoRepository.existsByCuidadorIdAndPacienteId(cuidador.getId(), paciente.getId())) {
            throw new RegraNegocioException("Esse vínculo já existe");
        }

        Vinculo vinculo = Vinculo.builder().cuidador(cuidador).paciente(paciente).build();
        return VinculoResponseDTO.from(vinculoRepository.save(vinculo));
    }

    public List<UsuarioResponseDTO> listarPacientesDoCuidador(Long cuidadorId) {
        usuarioService.buscarPorId(cuidadorId);
        return vinculoRepository.findByCuidadorId(cuidadorId).stream()
                .map(v -> UsuarioResponseDTO.from(v.getPaciente()))
                .toList();
    }

    /**
     * Regra de privacidade (LGPD): o paciente acessa os próprios dados;
     * o cuidador só acessa dados de pacientes vinculados a ele.
     */
    public void validarAcessoAoPaciente(Long solicitanteId, Long pacienteId) {
        if (solicitanteId.equals(pacienteId)) {
            return;
        }
        if (!vinculoRepository.existsByCuidadorIdAndPacienteId(solicitanteId, pacienteId)) {
            throw new AcessoNegadoException("Você não tem vínculo com esse paciente");
        }
    }
}