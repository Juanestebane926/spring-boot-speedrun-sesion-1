package com.curso.vacantes.config;

import com.curso.vacantes.model.Categoria;
import com.curso.vacantes.model.Vacante;
import com.curso.vacantes.repository.CategoriaRepository;
import com.curso.vacantes.repository.VacanteRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Sembrador de datos iniciales al arrancar la aplicación.
 * Permite probar inmediatamente endpoints y filtros sin configurar bases externas.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final VacanteRepository vacanteRepository;

    public DataInitializer(CategoriaRepository categoriaRepository, VacanteRepository vacanteRepository) {
        this.categoriaRepository = categoriaRepository;
        this.vacanteRepository = vacanteRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() == 0) {
            Categoria tecnologia = categoriaRepository.save(new Categoria("Tecnología"));
            Categoria administracion = categoriaRepository.save(new Categoria("Administración"));
            categoriaRepository.save(new Categoria("Diseño y Creatividad"));

            vacanteRepository.saveAll(List.of(
                    new Vacante(
                            "Desarrollador Java Junior",
                            "Tech Colombia",
                            "Apoyar el desarrollo de APIs REST con Spring Boot",
                            tecnologia
                    ),
                    new Vacante(
                            "Practicante de desarrollo",
                            "Software Escolar",
                            "Aprender y colaborar con el equipo de producto",
                            tecnologia
                    ),
                    new Vacante(
                            "Auxiliar Administrativo",
                            "Tech Colombia",
                            "Gestión de documentos y apoyo logístico",
                            administracion
                    )
            ));
        }
    }
}
