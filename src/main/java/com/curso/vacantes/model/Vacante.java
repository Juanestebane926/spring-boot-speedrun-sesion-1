package com.curso.vacantes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que mapea la tabla 'vacantes'.
 *
 * ARQUITECTURA RELACIONAL:
 * En una relación 1 a N, el lado propietario (owning side) es aquel cuya tabla física
 * contiene la llave foránea (FK). Aquí, la tabla 'vacantes' tiene la columna 'categoria_id'.
 * Por eso 'Vacante' lleva @ManyToOne y @JoinColumn.
 */
@Entity
@Table(name = "vacantes")
public class Vacante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 120)
    private String empresa;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    protected Vacante() {
        // Constructor vacío requerido por JPA.
    }

    public Vacante(String titulo, String empresa, String descripcion, Categoria categoria) {
        this.titulo = titulo;
        this.empresa = empresa;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
