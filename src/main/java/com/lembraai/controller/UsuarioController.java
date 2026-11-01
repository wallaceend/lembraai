package com.lembraai.controller;

import com.lembraai.dto.UsuarioCadastroDTO;
import com.lembraai.dto.UsuarioResponseDTO;
import com.lembraai.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(dto));
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscar(@PathVariable("id") Long id) {
        return usuarioService.buscar(id);
    }
}