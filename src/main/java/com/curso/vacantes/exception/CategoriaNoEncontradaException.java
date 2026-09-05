package com.curso.vacantes.exception;

public class CategoriaNoEncontradaException extends RuntimeException {
    public CategoriaNoEncontradaException(Long id) {
        super("Categoría no encontrada con id: " + id);
    }
}
