package com.lembraai.controller;

import com.lembraai.dto.UsuarioResponseDTO;
import com.lembraai.dto.VinculoRequestDTO;
import com.lembraai.dto.VinculoResponseDTO;
import com.lembraai.service.VinculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VinculoController {

    private final VinculoService vinculoService;

    @PostMapping("/vinculos")
    public ResponseEntity<VinculoResponseDTO> vincular(@Valid @RequestBody VinculoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vinculoService.vincular(dto));
    }

    @GetMapping("/cuidadores/{cuidadorId}/pacientes")
    public List<UsuarioResponseDTO> listarPacientes(@PathVariable("cuidadorId") Long cuidadorId) {
        return vinculoService.listarPacientesDoCuidador(cuidadorId);
    }
}