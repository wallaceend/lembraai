package com.lembraai.service;

import com.lembraai.dto.UsuarioCadastroDTO;
import com.lembraai.dto.UsuarioResponseDTO;
import com.lembraai.exception.RecursoNaoEncontradoException;
import com.lembraai.exception.RegraNegocioException;
import com.lembraai.model.Usuario;
import com.lembraai.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO cadastrar(UsuarioCadastroDTO dto) {
        String email = dto.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome().trim())
                .email(email)
                .senha(passwordEncoder.encode(dto.senha()))
                .perfil(dto.perfil())
                .build();

        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO buscar(Long id) {
        return UsuarioResponseDTO.from(buscarPorId(id));
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }
}