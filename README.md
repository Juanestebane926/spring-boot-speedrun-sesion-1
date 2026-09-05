# 🚀 Spring Boot Speed Run — Sesión 1: Relaciones JPA, DTOs y Reglas de Negocio

¡Bienvenidos a la primera sesión intensiva de Spring Boot!
En esta sesión vamos a dar el salto definitivo de datos aislados a un **modelo relacional profesional**: conectaremos vacantes con categorías, evitaremos ciclos infinitos en JSON, crearemos consultas derivadas (*Query Methods*) y protegeremos la integridad del negocio en la capa de servicios.

---

## 🏗️ La Arquitectura de 3 Capas que Debes Grabar en tu Mente

```text
CLIENTE (Thunder Client / Browser / Frontend)
           │
           │  Petición HTTP (GET /api/vacantes?categoriaId=1)
           ▼
┌────────────────────────────────────────────────────────┐
│ 1. CONTROLLER (VacanteController)                      │
│    "El Controller traduce HTTP"                        │
│    - Recibe la petición y extrae @RequestParam / Body  │
│    - NO toma decisiones de negocio                     │
└──────────────────────────┬─────────────────────────────┘
                           │ Invoca método del servicio
                           ▼
┌────────────────────────────────────────────────────────┐
│ 2. SERVICE (VacanteService / CategoriaService)         │
│    "El Service decide"                                 │
│    - Contiene las reglas del negocio                   │
│    - Si intentan borrar categoría con vacantes: 409    │
│    - Mapea Entidades a DTOs para cortar ciclos         │
└──────────────────────────┬─────────────────────────────┘
                           │ Llama al repositorio
                           ▼
┌────────────────────────────────────────────────────────┐
│ 3. REPOSITORY (VacanteRepository / CategoriaRepository)│
│    "El Repository persiste"                            │
│    - Spring Data JPA genera el SQL automáticamente     │
│    - Ejecuta Query Methods (findByCategoriaId)         │
└──────────────────────────┬─────────────────────────────┘
                           │ SQL generado
                           ▼
┌────────────────────────────────────────────────────────┐
│ BASE DE DATOS (H2 en memoria)                          │
│ Tabla VACANTES ──[FK: categoria_id]──> Tabla CATEGORIAS│
└────────────────────────────────────────────────────────┘
```

---

## ⚡ Cómo Empezar en GitHub Codespaces (0 Instalación)

1. Haz clic en el botón verde **`< > Code`** arriba a la derecha.
2. Selecciona la pestaña **Codespaces** y haz clic en **Create codespace on main**.
3. Espera unos segundos a que cargue VS Code en tu navegador. ¡Todo (Java 21, Maven, extensiones) ya está listo!

---

## 🧪 Cómo Evaluar Tu Código (Feedback Inmediato)

En la terminal integrada de VS Code (o pestaña de Tests en la barra lateral izquierda), ejecuta:

```bash
mvn test
```

Verás una suite de 7 pruebas unitarias y de integración. Tu objetivo en esta clase es entender cada concepto y lograr que todas las pruebas pasen a **verde**.

---

## 🎯 Las 4 Etapas del Laboratorio

### Etapa 1: La Relación Relacional (@ManyToOne)
- Revisa `com.curso.vacantes.model.Vacante`.
- Observa la anotación `@ManyToOne` y `@JoinColumn(name = "categoria_id")`.
- **Pregunta clave**: ¿Por qué `Vacante` es el lado propietario de la relación? Porque la tabla física `vacantes` es la que almacena la columna de la llave foránea (`categoria_id`).

### Etapa 2: DTOs Planos vs Ciclos Infinitos (StackOverflowError)
- Revisa `com.curso.vacantes.dto.VacanteResponse`.
- Nota que en lugar de incluir todo el objeto `Categoria`, exponemos campos planos: `categoriaId` y `categoriaNombre`.
- **Pregunta clave**: ¿Qué pasaría si serializáramos la entidad completa con Jackson? Al tener relaciones bidireccionales o cargas perezosas, Jackson entra en un bucle infinito que tumba la memoria del servidor. **El DTO es tu frontera segura.**

### Etapa 3: Consultas Derivadas (Query Methods)
- Abre `com.curso.vacantes.repository.VacanteRepository`.
- Fíjate en los métodos:
  ```java
  List<Vacante> findByCategoriaId(Long categoriaId);
  List<Vacante> findByEmpresaContainingIgnoreCase(String empresa);
  boolean existsByCategoriaId(Long categoriaId);
  ```
- **Pregunta clave**: ¿Dónde está el SQL? ¡No hay! Spring Data JPA lee el nombre del método como una oración en inglés y sintetiza la consulta optimizada en tiempo de ejecución.

### Etapa 4: Regla de Negocio Transaccional (409 Conflict)
- Abre `com.curso.vacantes.service.CategoriaService.java`.
- Ve al método `eliminar(Long id)`.
- Si alguien intenta borrar una categoría que aún tiene vacantes activas, debemos bloquear la acción y lanzar `CategoriaConVacantesException(id)`.
- Descomenta la validación en `CategoriaService` y vuelve a correr `mvn test`.

---

## 🌐 Pruebas Interactivas en Vivo

1. En la terminal corre la aplicación:
   ```bash
   mvn spring-boot:run
   ```
2. Abre el archivo `requests.http`.
3. Haz clic en **Send Request** sobre cada petición para ver las respuestas en tiempo real:
   - Prueba el filtro: `GET /api/vacantes?categoriaId=1`
   - Prueba la búsqueda por empresa: `GET /api/vacantes?empresa=Tech`
   - Prueba la regla de negocio: `DELETE /api/categorias/1` y confirma el código **`409 Conflict`**.

---

## 📤 Entrega y Calificación Automática

Cuando todos tus tests estén pasando:

```bash
git add .
git commit -m "feat: implementa relaciones jpa, query methods y reglas de negocio"
git push origin main
```

GitHub Actions evaluará tu código automáticamente y verás el check verde en tu repositorio. ¡A programar!
