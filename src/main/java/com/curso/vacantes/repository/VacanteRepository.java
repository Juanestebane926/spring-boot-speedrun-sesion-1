package com.curso.vacantes.repository;

import com.curso.vacantes.model.Vacante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para Vacante.
 *
 * CONSULTAS DERIVADAS (Query Methods):
 * Spring analiza el nombre del método como una oración en inglés y genera el SQL correspondiente:
 * - findByCategoriaId -> WHERE categoria_id = ?
 * - findByEmpresaContainingIgnoreCase -> WHERE LOWER(empresa) LIKE LOWER('%?%')
 * - existsByCategoriaId -> SELECT COUNT(*) > 0 WHERE categoria_id = ?
 */
@Repository
public interface VacanteRepository extends JpaRepository<Vacante, Long> {

    List<Vacante> findByCategoriaId(Long categoriaId);

    List<Vacante> findByEmpresaContainingIgnoreCase(String empresa);

    boolean existsByCategoriaId(Long categoriaId);
}
