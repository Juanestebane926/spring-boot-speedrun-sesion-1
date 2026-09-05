package com.curso.vacantes.service;

import com.curso.vacantes.dto.VacanteRequest;
import com.curso.vacantes.dto.VacanteResponse;
import com.curso.vacantes.exception.VacanteNoEncontradaException;
import com.curso.vacantes.model.Categoria;
import com.curso.vacantes.model.Vacante;
import com.curso.vacantes.repository.VacanteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VacanteService {

    private final VacanteRepository vacanteRepository;
    private final CategoriaService categoriaService;

    public VacanteService(VacanteRepository vacanteRepository, CategoriaService categoriaService) {
        this.vacanteRepository = vacanteRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional(readOnly = true)
    public List<VacanteResponse> listar(Long categoriaId, String empresa) {
        List<Vacante> vacantes;

        // Decisión de negocio: qué consulta derivada del repositorio invocar según los filtros recibidos
        if (categoriaId != null) {
            vacantes = vacanteRepository.findByCategoriaId(categoriaId);
        } else if (empresa != null && !empresa.isBlank()) {
            vacantes = vacanteRepository.findByEmpresaContainingIgnoreCase(empresa);
        } else {
            vacantes = vacanteRepository.findAll();
        }

        return vacantes.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VacanteResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public VacanteResponse crear(VacanteRequest request) {
        Categoria categoria = categoriaService.buscarEntidad(request.getCategoriaId());
        Vacante vacante = new Vacante(
                request.getTitulo(),
                request.getEmpresa(),
                request.getDescripcion(),
                categoria
        );
        return toResponse(vacanteRepository.save(vacante));
    }

    @Transactional
    public VacanteResponse actualizar(Long id, VacanteRequest request) {
        Vacante vacante = buscarEntidad(id);
        Categoria categoria = categoriaService.buscarEntidad(request.getCategoriaId());

        vacante.setTitulo(request.getTitulo());
        vacante.setEmpresa(request.getEmpresa());
        vacante.setDescripcion(request.getDescripcion());
        vacante.setCategoria(categoria);

        return toResponse(vacanteRepository.save(vacante));
    }

    @Transactional
    public void eliminar(Long id) {
        Vacante vacante = buscarEntidad(id);
        vacanteRepository.delete(vacante);
    }

    public Vacante buscarEntidad(Long id) {
        return vacanteRepository.findById(id)
                .orElseThrow(() -> new VacanteNoEncontradaException(id));
    }

    private VacanteResponse toResponse(Vacante vacante) {
        return new VacanteResponse(
                vacante.getId(),
                vacante.getTitulo(),
                vacante.getEmpresa(),
                vacante.getDescripcion(),
                vacante.getCategoria().getId(),
                vacante.getCategoria().getNombre()
        );
    }
}
