package com.lembraai.dto;

import com.lembraai.model.Usuario;
import com.lembraai.model.enums.Perfil;

public record UsuarioResponseDTO(Long id, String nome, String email, Perfil perfil) {

    public static UsuarioResponseDTO from(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getPerfil());
    }
}