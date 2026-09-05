package com.curso.vacantes.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VacanteNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarVacanteNoEncontrada(
            VacanteNoEncontradaException exception,
            HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarCategoriaNoEncontrada(
            CategoriaNoEncontradaException exception,
            HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(CategoriaConVacantesException.class)
    public ResponseEntity<ApiError> manejarCategoriaConVacantes(
            CategoriaConVacantesException exception,
            HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fields.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        return respuesta(HttpStatus.BAD_REQUEST,
                "Hay campos inválidos en la petición", request, fields);
    }

    private ResponseEntity<ApiError> respuesta(HttpStatus status, String message,
            HttpServletRequest request, Map<String, String> fields) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fields
        );
        return ResponseEntity.status(status).body(body);
    }
}
