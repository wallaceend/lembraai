package com.lembraai.controller;

import com.lembraai.dto.ChatRequestDTO;
import com.lembraai.dto.ChatResponseDTO;
import com.lembraai.service.AssistenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assistente")
@RequiredArgsConstructor
public class AssistenteController {

    private final AssistenteService assistenteService;

    @PostMapping("/chat")
    public ChatResponseDTO chat(@Valid @RequestBody ChatRequestDTO dto) {
        return assistenteService.responder(dto.mensagem());
    }
}