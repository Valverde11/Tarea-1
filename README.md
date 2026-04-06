# Tarea 1 - Laboratorio de Rendimiento de Estructuras de Datos
**CE-1103 Algoritmos y Estructuras de Datos I**

## Estructura del proyecto

```
src/
  estructuras/    → Implementaciones de todas las estructuras de datos
  benchmark/      → Benchmark, escenarios y exportación JSON
  graficos/       → Generador de gráficos en Java
resultados/       → Archivos JSON generados (5 corridas)
graficos/         → Imágenes PNG de los gráficos
```

## Estructuras implementadas

- `MiArray` — Arreglo dinámico genérico
- `ListaSimple` — Lista simplemente enlazada
- `ListaDoble` — Lista doblemente enlazada
- `DoubleEndedList` — Lista con referencia a cabeza y cola
- `ListaCircular` — Lista circular enlazada
- `StackArray` / `StackLista` — Pila con array y con lista
- `QueueArray` / `QueueLista` — Cola con array y con lista

## Cómo compilar

Desde la raíz del proyecto:

```bash
javac -d out -sourcepath src src/benchmark/Benchmark.java src/benchmark/Escenarios.java src/graficos/GeneradorGraficos.java
```

## Cómo ejecutar

### 1. Benchmark principal (genera los JSON en /resultados)
```bash
java -cp out benchmark.Benchmark
```

### 2. Escenarios Stack vs Queue
```bash
java -cp out benchmark.Escenarios
```

### 3. Generar gráficos (requiere haber corrido el benchmark primero)
```bash
java -cp out graficos.GeneradorGraficos
```

## Gráficos generados

| Archivo | Descripción |
|---|---|
| `tiempo_insercion_inicio.png` | Tiempo de inserción al inicio vs N |
| `tiempo_insercion_final.png` | Tiempo de inserción al final vs N |
| `tiempo_insercion_intermedia.png` | Tiempo de inserción intermedia vs N |
| `tiempo_busqueda.png` | Tiempo de búsqueda vs N |
| `tiempo_acceso_indice.png` | Tiempo de acceso por índice vs N |
| `tiempo_eliminacion.png` | Tiempo de eliminación vs N |
| `tiempo_reemplazo.png` | Tiempo de reemplazo vs N |
| `memoria_vs_n.png` | Uso de memoria vs N |
| `stack_vs_queue.png` | Comparación Stack vs Queue (push/pop/enqueue/dequeue) |

## Metodología

- Tamaños de prueba: 10, 100, 1000, 10 000 elementos
- 5 corridas independientes, cada una exportada a JSON en `/resultados/`
- Los gráficos calculan el promedio de las 5 corridas automáticamente
