package com.lembraai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResponse(int status, String mensagem, Map<String, String> campos, LocalDateTime timestamp) {}

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException ex) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException ex) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponse> acessoNegado(AcessoNegadoException ex) {
        return montar(HttpStatus.FORBIDDEN, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.put(e.getField(), e.getDefaultMessage()));
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos", campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        return montar(HttpStatus.BAD_REQUEST,
                "JSON inválido ou com valores incorretos (confira datas, horários e perfil)", null);
    }

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status)
                .body(new ErroResponse(status.value(), mensagem, campos, LocalDateTime.now()));
    }
}