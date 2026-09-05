package com.curso.vacantes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VacanteRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 120, message = "El título no puede superar los 120 caracteres")
    private String titulo;

    @NotBlank(message = "La empresa es obligatoria")
    @Size(max = 120, message = "La empresa no puede superar los 120 caracteres")
    private String empresa;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La vacante debe pertenecer a una categoría obligatoriamente")
    private Long categoriaId;

    public VacanteRequest() {
    }

    public VacanteRequest(String titulo, String empresa, String descripcion, Long categoriaId) {
        this.titulo = titulo;
        this.empresa = empresa;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
