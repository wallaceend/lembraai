package com.lembraai.service;

import com.lembraai.dto.ChatResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AssistenteService {

    public ChatResponseDTO responder(String mensagem) {
        String texto = mensagem.toLowerCase();
        String resposta;

        if (texto.contains("esqueci") || texto.contains("perdi")) {
            resposta = "Sem problema! Se já passou muito do horário, evite dobrar a dose e consulte o médico ou o cuidador.";
        } else if (texto.contains("remédio") || texto.contains("remedio") || texto.contains("medicamento")) {
            resposta = "Seus próximos medicamentos aparecem na lista de lembretes. Quando tomar, toque em \"Tomei\" para confirmar.";
        } else if (texto.contains("hora") || texto.contains("quando")) {
            resposta = "Confira na sua lista de lembretes o próximo horário. Eu aviso quando estiver chegando!";
        } else {
            resposta = "Olá! Sou o assistente do LembraAI. Posso ajudar com dúvidas sobre seus lembretes e medicamentos.";
        }
        return new ChatResponseDTO(resposta);
    }
}