package com.curso.vacantes.exception;

public class VacanteNoEncontradaException extends RuntimeException {
    public VacanteNoEncontradaException(Long id) {
        super("Vacante no encontrada con id: " + id);
    }
}
