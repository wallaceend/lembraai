package com.lembraai.controller;

import com.lembraai.dto.MedicamentoRequestDTO;
import com.lembraai.dto.MedicamentoResponseDTO;
import com.lembraai.service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @PostMapping("/medicamentos")
    public ResponseEntity<MedicamentoResponseDTO> criar(
            @RequestParam("solicitanteId") Long solicitanteId,
            @Valid @RequestBody MedicamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoService.criar(solicitanteId, dto));
    }

    @GetMapping("/pacientes/{pacienteId}/medicamentos")
    public List<MedicamentoResponseDTO> listarPorPaciente(
            @PathVariable("pacienteId") Long pacienteId,
            @RequestParam("solicitanteId") Long solicitanteId) {
        return medicamentoService.listarPorPaciente(pacienteId, solicitanteId);
    }

    @GetMapping("/medicamentos/{id}")
    public MedicamentoResponseDTO buscar(
            @PathVariable("id") Long id,
            @RequestParam("solicitanteId") Long solicitanteId) {
        return medicamentoService.buscar(id, solicitanteId);
    }

    @PutMapping("/medicamentos/{id}")
    public MedicamentoResponseDTO atualizar(
            @PathVariable("id") Long id,
            @RequestParam("solicitanteId") Long solicitanteId,
            @Valid @RequestBody MedicamentoRequestDTO dto) {
        return medicamentoService.atualizar(id, solicitanteId, dto);
    }

    @DeleteMapping("/medicamentos/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable("id") Long id,
            @RequestParam("solicitanteId") Long solicitanteId) {
        medicamentoService.deletar(id, solicitanteId);
        return ResponseEntity.noContent().build();
    }
}