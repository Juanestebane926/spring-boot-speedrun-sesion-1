package com.curso.vacantes.repository;

import com.curso.vacantes.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la entidad Categoria.
 * Spring genera automáticamente las operaciones CRUD básicas en tiempo de ejecución.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
