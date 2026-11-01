package com.lembraai.controller;

import com.lembraai.dto.LembreteResponseDTO;
import com.lembraai.model.enums.StatusLembrete;
import com.lembraai.service.LembreteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LembreteController {

    private final LembreteService lembreteService;

    @GetMapping("/pacientes/{pacienteId}/lembretes")
    public List<LembreteResponseDTO> listar(
            @PathVariable("pacienteId") Long pacienteId,
            @RequestParam("solicitanteId") Long solicitanteId,
            @RequestParam(value = "status", required = false) StatusLembrete status) {
        return lembreteService.listarDoPaciente(pacienteId, solicitanteId, status);
    }

    @PatchMapping("/lembretes/{id}/tomei")
    public LembreteResponseDTO tomei(
            @PathVariable("id") Long id,
            @RequestParam("solicitanteId") Long solicitanteId) {
        return lembreteService.confirmarTomei(id, solicitanteId);
    }
}