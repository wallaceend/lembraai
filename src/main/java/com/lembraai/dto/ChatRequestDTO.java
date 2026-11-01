package com.lembraai.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(
        @NotBlank(message = "A mensagem é obrigatória") String mensagem
) {}