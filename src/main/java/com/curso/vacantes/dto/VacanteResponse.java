package com.curso.vacantes.dto;

/**
 * DTO de salida para vacantes.
 *
 * ARQUITECTURA CRÍTICA:
 * Nunca devolvemos la entidad Categoria completa dentro de la Vacante.
 * Si devolviéramos la entidad, Jackson intentaría serializar la relación y provocaría
 * un ciclo infinito (StackOverflowError).
 * Al exponer campos planos (categoriaId, categoriaNombre), cortamos el ciclo y blindamos el contrato HTTP.
 */
public class VacanteResponse {

    private Long id;
    private String titulo;
    private String empresa;
    private String descripcion;
    private Long categoriaId;
    private String categoriaNombre;

    public VacanteResponse() {
    }

    public VacanteResponse(Long id, String titulo, String empresa, String descripcion, Long categoriaId, String categoriaNombre) {
        this.id = id;
        this.titulo = titulo;
        this.empresa = empresa;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
}
