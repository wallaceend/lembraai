package com.lembraai.controller;

import com.lembraai.dto.PainelPacienteDTO;
import com.lembraai.dto.RelatorioSemanalDTO;
import com.lembraai.service.AcompanhamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AcompanhamentoController {

    private final AcompanhamentoService acompanhamentoService;

    @GetMapping("/cuidadores/{cuidadorId}/painel")
    public List<PainelPacienteDTO> painel(@PathVariable("cuidadorId") Long cuidadorId) {
        return acompanhamentoService.painelDoCuidador(cuidadorId);
    }

    @GetMapping("/pacientes/{pacienteId}/relatorio/semanal")
    public RelatorioSemanalDTO relatorioSemanal(
            @PathVariable("pacienteId") Long pacienteId,
            @RequestParam("solicitanteId") Long solicitanteId) {
        return acompanhamentoService.relatorioSemanal(pacienteId, solicitanteId);
    }
}