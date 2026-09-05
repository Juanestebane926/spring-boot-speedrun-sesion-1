package com.curso.vacantes.exception;

public class CategoriaConVacantesException extends RuntimeException {
    public CategoriaConVacantesException(Long id) {
        super("No se puede eliminar la categoría con id " + id + " porque tiene vacantes asociadas. Debe reasignar o eliminar las vacantes primero.");
    }
}
