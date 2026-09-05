package com.curso.vacantes.service;

import com.curso.vacantes.dto.CategoriaRequest;
import com.curso.vacantes.dto.CategoriaResponse;
import com.curso.vacantes.exception.CategoriaConVacantesException;
import com.curso.vacantes.exception.CategoriaNoEncontradaException;
import com.curso.vacantes.model.Categoria;
import com.curso.vacantes.repository.CategoriaRepository;
import com.curso.vacantes.repository.VacanteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Capa Service: Contiene la lógica de negocio y coordina las entidades y repositorios.
 * Regla de oro: "El controller traduce HTTP, el service decide y el repository persiste".
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final VacanteRepository vacanteRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, VacanteRepository vacanteRepository) {
        this.categoriaRepository = categoriaRepository;
        this.vacanteRepository = vacanteRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria(request.getNombre());
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarEntidad(id);

        // Regla de negocio transaccional:
        // Si la categoría tiene vacantes asociadas, NO debe permitirse su eliminación.
        if (vacanteRepository.existsByCategoriaId(id)) {
            throw new CategoriaConVacantesException(id);
        }

        categoriaRepository.delete(categoria);
    }

    public Categoria buscarEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException(id));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre());
    }
}
