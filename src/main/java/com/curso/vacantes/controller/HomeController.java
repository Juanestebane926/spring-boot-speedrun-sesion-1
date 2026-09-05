package com.curso.vacantes.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de bienvenida en la raíz '/' para confirmar que el servidor está levantado
 * y guiar al estudiante hacia los endpoints principales.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
                "status", "UP",
                "mensaje", "API de Vacantes lista - Speed Run Sesión 1",
                "endpoints_principales", List.of(
                        "/api/vacantes",
                        "/api/categorias",
                        "/h2-console"
                )
        );
    }
}
