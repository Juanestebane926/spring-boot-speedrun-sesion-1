package com.curso.vacantes.controller;

import com.curso.vacantes.dto.VacanteRequest;
import com.curso.vacantes.dto.VacanteResponse;
import com.curso.vacantes.service.VacanteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para Vacantes.
 * Recibe parámetros por ruta (@PathVariable), parámetros de consulta (@RequestParam)
 * y cuerpo de petición (@RequestBody).
 */
@RestController
@RequestMapping("/api/vacantes")
public class VacanteController {

    private final VacanteService vacanteService;

    public VacanteController(VacanteService vacanteService) {
        this.vacanteService = vacanteService;
    }

    @GetMapping
    public List<VacanteResponse> listar(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String empresa) {
        return vacanteService.listar(categoriaId, empresa);
    }

    @GetMapping("/{id}")
    public VacanteResponse buscarPorId(@PathVariable Long id) {
        return vacanteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacanteResponse crear(@Valid @RequestBody VacanteRequest request) {
        return vacanteService.crear(request);
    }

    @PutMapping("/{id}")
    public VacanteResponse actualizar(@PathVariable Long id, @Valid @RequestBody VacanteRequest request) {
        return vacanteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        vacanteService.eliminar(id);
    }
}
