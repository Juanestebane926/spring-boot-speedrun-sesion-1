package com.curso.vacantes;

import com.curso.vacantes.dto.CategoriaRequest;
import com.curso.vacantes.dto.CategoriaResponse;
import com.curso.vacantes.dto.VacanteRequest;
import com.curso.vacantes.dto.VacanteResponse;
import com.curso.vacantes.exception.CategoriaConVacantesException;
import com.curso.vacantes.service.CategoriaService;
import com.curso.vacantes.service.VacanteService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

/**
 * Suite de Autograding para Sesión 1:
 * Evalúa las 5 etapas arquitectónicas del backend relacional.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VacantesApplicationTests {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private VacanteService vacanteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("Etapa 1: Creación de Categoría e ID autogenerado en JPA")
    void test01_crearYBuscarCategoria() {
        CategoriaResponse categoria = categoriaService.crear(new CategoriaRequest("Ciberseguridad"));
        assertNotNull(categoria.getId(), "La categoría guardada debe tener un ID autogenerado por la base de datos.");
        assertEquals("Ciberseguridad", categoria.getNombre());

        CategoriaResponse encontrada = categoriaService.buscarPorId(categoria.getId());
        assertEquals("Ciberseguridad", encontrada.getNombre(), "Debe poder recuperarse la categoría por su ID.");
    }

    @Test
    @Order(2)
    @DisplayName("Etapa 2: Relación @ManyToOne y DTO plano (prevenir ciclos infinitos)")
    void test02_crearVacanteConCategoriaYVerificarDtoPlano() {
        CategoriaResponse cat = categoriaService.crear(new CategoriaRequest("Backend Development"));

        VacanteResponse vacante = vacanteService.crear(new VacanteRequest(
                "Ingeniero Java Spring",
                "Globant",
                "Desarrollo de microservicios con JPA",
                cat.getId()
        ));

        assertNotNull(vacante.getId(), "La vacante creada debe tener un ID asignado.");
        assertEquals(cat.getId(), vacante.getCategoriaId(), "VacanteResponse debe incluir 'categoriaId' de forma plana.");
        assertEquals("Backend Development", vacante.getCategoriaNombre(), "VacanteResponse debe incluir 'categoriaNombre' de forma plana.");
    }

    @Test
    @Order(3)
    @DisplayName("Etapa 3A: Query Method findByCategoriaId en Repositorio")
    void test03_filtrarPorCategoriaId() {
        CategoriaResponse cat = categoriaService.crear(new CategoriaRequest("DevOps"));
        vacanteService.crear(new VacanteRequest("DevOps Engineer", "Cloud Solutions", "Docker y Kubernetes", cat.getId()));

        List<VacanteResponse> filtradas = vacanteService.listar(cat.getId(), null);
        assertFalse(filtradas.isEmpty(), "El filtro findByCategoriaId debe devolver las vacantes de la categoría solicitada.");
        assertEquals("DevOps Engineer", filtradas.get(0).getTitulo());
    }

    @Test
    @Order(4)
    @DisplayName("Etapa 3B: Query Method findByEmpresaContainingIgnoreCase en Repositorio")
    void test04_filtrarPorEmpresaIgnorandoMayusculas() {
        CategoriaResponse cat = categoriaService.crear(new CategoriaRequest("QA Automation"));
        vacanteService.crear(new VacanteRequest("QA Lead", "MercadoLibre", "Pruebas end-to-end", cat.getId()));

        List<VacanteResponse> filtradas = vacanteService.listar(null, "mercadolibre");
        assertFalse(filtradas.isEmpty(), "La búsqueda parcial por empresa debe ignorar mayúsculas y minúsculas.");
        assertEquals("MercadoLibre", filtradas.get(0).getEmpresa());
    }

    @Test
    @Order(5)
    @DisplayName("Etapa 4: Regla de negocio en Service (Lanzar 409 Conflict si la categoría tiene vacantes)")
    void test05_reglaDeNegocioBloqueaEliminacionDeCategoriaConVacantes() {
        CategoriaResponse cat = categoriaService.crear(new CategoriaRequest("Data Science"));
        vacanteService.crear(new VacanteRequest("Data Analyst", "Fintech Latam", "Modelos predictivos", cat.getId()));

        assertThrows(CategoriaConVacantesException.class, () -> {
            categoriaService.eliminar(cat.getId());
        }, "❌ REGLA DE NEGOCIO: No se debe permitir eliminar una categoría que tenga vacantes activas. Debe lanzar CategoriaConVacantesException.");
    }

    @Test
    @Order(6)
    @DisplayName("Etapa 4B: Eliminación exitosa de categoría huérfana (sin vacantes)")
    void test06_eliminarCategoriaSinVacantesPermitido() {
        CategoriaResponse catVacia = categoriaService.crear(new CategoriaRequest("Categoria Temporal"));
        assertDoesNotThrow(() -> categoriaService.eliminar(catVacia.getId()),
                "Debe ser posible eliminar una categoría que no tiene vacantes asociadas.");
    }

    @Test
    @Order(7)
    @DisplayName("Etapa 5: Contrato HTTP REST - Rechazo de datos inválidos con 400 Bad Request")
    void test07_rechazaCreacionInvalidaCon400() throws Exception {
        mockMvc.perform(post("/api/vacantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"\",\"empresa\":\"\",\"categoriaId\":null}"))
                .andExpect(status().isBadRequest());
    }
}
