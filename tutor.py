#!/usr/bin/env python3
import subprocess
import sys
import time
import os
import re

HINTS = {
    "test01": {
        "etapa": "Etapa 1: Creación de Categoría e ID autogenerado en JPA",
        "archivo": "src/main/java/com/curso/vacantes/model/Categoria.java",
        "metodo": "Entidad Categoria y CategoriaRepository",
        "pista": "Asegurate de que la clase Categoria esté anotada con @Entity, tenga un @Id con @GeneratedValue(strategy = GenerationType.IDENTITY) y su repositorio extienda JpaRepository<Categoria, Long>."
    },
    "test02": {
        "etapa": "Etapa 2: Relación @ManyToOne y DTO plano (prevenir ciclos infinitos)",
        "archivo": "src/main/java/com/curso/vacantes/model/Vacante.java y dto/VacanteResponse.java",
        "metodo": "Relación @ManyToOne y mapeo en VacanteService.toResponse()",
        "pista": "En Vacante.java agrega @ManyToOne y @JoinColumn(name = \"categoria_id\"). En VacanteResponse expone categoriaId y categoriaNombre planos en vez de toda la entidad Categoria para evitar el ciclo infinito de Jackson."
    },
    "test03": {
        "etapa": "Etapa 3A: Query Method findByCategoriaId en Repositorio",
        "archivo": "src/main/java/com/curso/vacantes/repository/VacanteRepository.java",
        "metodo": "findByCategoriaId(Long categoriaId)",
        "pista": "En la interfaz VacanteRepository declara el método: List<Vacante> findByCategoriaId(Long categoriaId);. Spring Data JPA creará la consulta SQL automáticamente."
    },
    "test04": {
        "etapa": "Etapa 3B: Query Method findByEmpresaContainingIgnoreCase",
        "archivo": "src/main/java/com/curso/vacantes/repository/VacanteRepository.java",
        "metodo": "findByEmpresaContainingIgnoreCase(String empresa)",
        "pista": "En VacanteRepository agrega: List<Vacante> findByEmpresaContainingIgnoreCase(String empresa); para permitir búsquedas parciales sin importar mayúsculas."
    },
    "test05": {
        "etapa": "Etapa 4: Regla de negocio en Service (409 Conflict si hay vacantes)",
        "archivo": "src/main/java/com/curso/vacantes/service/CategoriaService.java",
        "metodo": "eliminar(Long id)",
        "pista": "Antes de llamar a categoriaRepository.delete(categoria), valida si tiene vacantes activas:\n   if (vacanteRepository.existsByCategoriaId(id)) {\n       throw new CategoriaConVacantesException(id);\n   }"
    },
    "test06": {
        "etapa": "Etapa 4B: Eliminación exitosa de categoría sin vacantes",
        "archivo": "src/main/java/com/curso/vacantes/service/CategoriaService.java",
        "metodo": "eliminar(Long id)",
        "pista": "Si la categoría no tiene vacantes asociadas, debe eliminarse con normalidad invocando categoriaRepository.delete(categoria)."
    },
    "test07": {
        "etapa": "Etapa 5: Contrato HTTP REST - Validación 400 Bad Request",
        "archivo": "src/main/java/com/curso/vacantes/controller/VacanteController.java",
        "metodo": "crear(@Valid @RequestBody VacanteRequest request)",
        "pista": "Verifica que el método crear en VacanteController tenga las anotaciones @Valid y @RequestBody para que Spring valide los campos obligatorios del DTO."
    }
}

def ejecutar_pruebas():
    os.system('clear' if os.name == 'posix' else 'cls')
    print("\033[1;36m" + "="*65 + "\033[0m")
    print("\033[1;36m       🎓 TUTOR INTERACTIVO - SPEED RUN SPRING BOOT\033[0m")
    print("\033[1;36m" + "="*65 + "\033[0m")
    print("⏳ Ejecutando suite de pruebas...\n")

    mvn_cmd = "./mvnw" if os.path.exists("./mvnw") else "mvn"
    proc = subprocess.run([mvn_cmd, "test", "-Dtest=VacantesApplicationTests", "-q"],
                          stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

    salida = proc.stdout + proc.stderr
    
    # Buscar qué tests fallaron
    fallas = re.findall(r'test0[1-7]\w*', salida)
    
    # Evaluar estado de cada test
    pasaron_todos = (proc.returncode == 0)
    
    for key, info in sorted(HINTS.items()):
        falla_este = any(key in f for f in fallas)
        if not falla_este and (pasaron_todos or key not in "".join(fallas)):
            print(f"  \033[1;32m[✅ PASÓ]\033[0m {info['etapa']}")
        else:
            print(f"  \033[1;31m[❌ PENDIENTE]\033[0m {info['etapa']}")

    print("\n" + "\033[1;36m" + "-"*65 + "\033[0m")

    if pasaron_todos:
        print("\033[1;32m🎉 ¡EXCELENTE! Todos los ejercicios están aprobados (100/100).\033[0m")
        print("Ya podés guardar tus cambios con:")
        print("   \033[1;33mgit add . && git commit -m \"feat: completa sesion 1\" && git push\033[0m\n")
    else:
        # Mostrar la primera pista relevante
        for key, info in sorted(HINTS.items()):
            if any(key in f for f in fallas):
                print(f"\033[1;33m💡 PISTA PARA AVANZAR:\033[0m")
                print(f"📍 \033[1mArchivo a revisar:\033[0m {info['archivo']}")
                print(f"📍 \033[1mMétodo:\033[0m            {info['metodo']}")
                print(f"\n📝 \033[1;37mInstrucción:\033[0m\n   {info['pista']}")
                break
    print("\033[1;36m" + "="*65 + "\033[0m\n")

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] in ["--watch", "-w"]:
        print("👀 Modo observación activo. Presioná Ctrl+C para salir.")
        ultimo_mtime = 0
        while True:
            # Buscar último cambio en src/
            mtime_max = 0
            for root, _, files in os.walk("src"):
                for f in files:
                    if f.endswith(".java"):
                        p = os.path.join(root, f)
                        mtime_max = max(mtime_max, os.path.getmtime(p))
            if mtime_max > ultimo_mtime:
                ultimo_mtime = mtime_max
                ejecutar_pruebas()
            time.sleep(1)
    else:
        ejecutar_pruebas()
