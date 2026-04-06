package benchmark;

import estructuras.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Escenario 1: Historial de acciones (Undo) - Stack es correcto, Queue es
 * incorrecto.
 * Escenario 2: Sistema de atención FIFO - Queue es correcto, Stack es
 * incorrecto.
 *
 * Mide tiempos de inserción y extracción para cada combinación y
 * exporta los resultados a JSON para que GeneradorGraficos los grafique.
 */
public class Escenarios {

    private static final int[] TAMANIOS = { 10, 100, 1000, 10000 };
    private static final int REPETICIONES = 5;

    public static void main(String[] args) {
        new java.io.File("resultados").mkdir();

        // ── Escenario 1: Undo ────────────────────────────────────────────
        System.out.println("========================================");
        System.out.println("  ESCENARIO 1: HISTORIAL DE ACCIONES (UNDO)");
        System.out.println("========================================");
        demoUndoStack();
        demoUndoQueue();

        // ── Escenario 2: FIFO ────────────────────────────────────────────
        System.out.println("\n========================================");
        System.out.println("  ESCENARIO 2: SISTEMA DE ATENCIÓN (FIFO)");
        System.out.println("========================================");
        demoAtencionQueue();
        demoAtencionStack();

        // ── Benchmark de escenarios (mide tiempos y exporta JSON) ────────
        System.out.println("\n========================================");
        System.out.println("  BENCHMARK DE ESCENARIOS");
        System.out.println("========================================");

        for (int corrida = 1; corrida <= REPETICIONES; corrida++) {
            System.out.println("=== Corrida " + corrida + " ===");
            List<ResultadoBenchmark> resultados = new ArrayList<>();

            for (int n : TAMANIOS) {
                resultados.addAll(benchmarkEscenario1(n));
                resultados.addAll(benchmarkEscenario2(n));
            }

            exportarJSON(resultados, "resultados/escenarios_corrida_" + corrida + ".json");
        }

        System.out.println("\nBenchmark de escenarios completado. JSON en /resultados/");
    }

    // ─────────────────────────────────────────────────────────────────────
    // DEMOS (muestran el comportamiento correcto vs incorrecto en consola)
    // ─────────────────────────────────────────────────────────────────────

    private static void demoUndoStack() {
        System.out.println("\n[Stack con Array - CORRECTO para Undo]");
        StackArray<String> historial = new StackArray<>(10);
        String[] acciones = { "Escribir 'Hola'", "Negrita", "Cambiar fuente", "Insertar imagen", "Guardar" };
        for (String accion : acciones) {
            historial.push(accion);
            System.out.println("  Acción realizada: " + accion);
        }
        System.out.println("\n  -- Deshacer acciones --");
        for (int i = 0; i < 3 && !historial.estaVacio(); i++)
            System.out.println("  Deshecho: " + historial.pop());

        System.out.println("\n[Stack con Lista - CORRECTO para Undo]");
        StackLista<String> historialLista = new StackLista<>();
        for (String accion : acciones)
            historialLista.push(accion);
        for (int i = 0; i < 3 && !historialLista.estaVacio(); i++)
            System.out.println("  Deshecho: " + historialLista.pop());
    }

    private static void demoUndoQueue() {
        System.out.println("\n[Queue con Array - INCORRECTO para Undo]");
        QueueArray<String> historial = new QueueArray<>(10);
        String[] acciones = { "Escribir 'Hola'", "Negrita", "Cambiar fuente", "Insertar imagen", "Guardar" };
        for (String accion : acciones)
            historial.enqueue(accion);
        System.out.println("  -- Intentar deshacer con Queue (saca la MÁS ANTIGUA, no la última) --");
        for (int i = 0; i < 3 && !historial.estaVacia(); i++)
            System.out.println(
                    "  'Deshecho' (incorrecto): " + historial.dequeue() + "  <-- Debería ser la última acción!");

        System.out.println("\n[Queue con Lista - INCORRECTO para Undo]");
        QueueLista<String> historialLista = new QueueLista<>();
        for (String accion : acciones)
            historialLista.enqueue(accion);
        for (int i = 0; i < 3 && !historialLista.estaVacia(); i++)
            System.out.println("  'Deshecho' (incorrecto): " + historialLista.dequeue() + "  <-- Orden equivocado!");
    }

    private static void demoAtencionQueue() {
        System.out.println("\n[Queue con Array - CORRECTO para atención FIFO]");
        QueueArray<String> fila = new QueueArray<>(10);
        String[] clientes = { "Cliente A", "Cliente B", "Cliente C", "Cliente D", "Cliente E" };
        for (String cliente : clientes) {
            fila.enqueue(cliente);
            System.out.println("  Llegó: " + cliente);
        }
        System.out.println("\n  -- Atendiendo clientes --");
        for (int i = 0; i < 3 && !fila.estaVacia(); i++)
            System.out.println("  Atendido: " + fila.dequeue() + "  (el primero en llegar)");

        System.out.println("\n[Queue con Lista - CORRECTO para atención FIFO]");
        QueueLista<String> filaLista = new QueueLista<>();
        for (String cliente : clientes)
            filaLista.enqueue(cliente);
        for (int i = 0; i < 3 && !filaLista.estaVacia(); i++)
            System.out.println("  Atendido: " + filaLista.dequeue());
    }

    private static void demoAtencionStack() {
        System.out.println("\n[Stack con Array - INCORRECTO para atención FIFO]");
        StackArray<String> fila = new StackArray<>(10);
        String[] clientes = { "Cliente A", "Cliente B", "Cliente C", "Cliente D", "Cliente E" };
        for (String cliente : clientes)
            fila.push(cliente);
        System.out.println("  -- Atendiendo con Stack (atiende al ÚLTIMO en llegar) --");
        for (int i = 0; i < 3 && !fila.estaVacio(); i++)
            System.out.println("  'Atendido' (incorrecto): " + fila.pop() + "  <-- Debería ser el primero en llegar!");

        System.out.println("\n[Stack con Lista - INCORRECTO para atención FIFO]");
        StackLista<String> filaLista = new StackLista<>();
        for (String cliente : clientes)
            filaLista.push(cliente);
        for (int i = 0; i < 3 && !filaLista.estaVacio(); i++)
            System.out.println("  'Atendido' (incorrecto): " + filaLista.pop() + "  <-- Orden equivocado!");
    }

    // ─────────────────────────────────────────────────────────────────────
    // BENCHMARK ESCENARIO 1: Undo
    // Mide insertar N acciones y deshacer N acciones con Stack y Queue
    // ─────────────────────────────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkEscenario1(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();

        // Stack Array - insertar (push)
        res.add(medir("E1_StackArray", "insertar", n, () -> {
            StackArray<Integer> s = new StackArray<>(n);
            for (int i = 0; i < n; i++)
                s.push(i);
        }, new StackArray<Integer>(n).usoMemoria()));

        // Stack Array - deshacer (pop)
        res.add(medir("E1_StackArray", "deshacer", n, () -> {
            StackArray<Integer> s = new StackArray<>(n);
            for (int i = 0; i < n; i++)
                s.push(i);
            for (int i = 0; i < n; i++)
                s.pop();
        }, new StackArray<Integer>(n).usoMemoria()));

        // Stack Lista - insertar
        res.add(medir("E1_StackLista", "insertar", n, () -> {
            StackLista<Integer> s = new StackLista<>();
            for (int i = 0; i < n; i++)
                s.push(i);
        }, new StackLista<Integer>().usoMemoria()));

        // Stack Lista - deshacer
        res.add(medir("E1_StackLista", "deshacer", n, () -> {
            StackLista<Integer> s = new StackLista<>();
            for (int i = 0; i < n; i++)
                s.push(i);
            for (int i = 0; i < n; i++)
                s.pop();
        }, new StackLista<Integer>().usoMemoria()));

        // Queue Array - insertar (incorrecto para undo)
        res.add(medir("E1_QueueArray", "insertar", n, () -> {
            QueueArray<Integer> q = new QueueArray<>(n);
            for (int i = 0; i < n; i++)
                q.enqueue(i);
        }, new QueueArray<Integer>(n).usoMemoria()));

        // Queue Array - "deshacer" (dequeue - orden incorrecto)
        res.add(medir("E1_QueueArray", "deshacer", n, () -> {
            QueueArray<Integer> q = new QueueArray<>(n);
            for (int i = 0; i < n; i++)
                q.enqueue(i);
            for (int i = 0; i < n; i++)
                q.dequeue();
        }, new QueueArray<Integer>(n).usoMemoria()));

        // Queue Lista - insertar
        res.add(medir("E1_QueueLista", "insertar", n, () -> {
            QueueLista<Integer> q = new QueueLista<>();
            for (int i = 0; i < n; i++)
                q.enqueue(i);
        }, new QueueLista<Integer>().usoMemoria()));

        // Queue Lista - "deshacer"
        res.add(medir("E1_QueueLista", "deshacer", n, () -> {
            QueueLista<Integer> q = new QueueLista<>();
            for (int i = 0; i < n; i++)
                q.enqueue(i);
            for (int i = 0; i < n; i++)
                q.dequeue();
        }, new QueueLista<Integer>().usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────────────────────────────
    // BENCHMARK ESCENARIO 2: FIFO
    // Mide insertar N clientes y atender N clientes con Queue y Stack
    // ─────────────────────────────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkEscenario2(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();

        // Queue Array - encolar
        res.add(medir("E2_QueueArray", "encolar", n, () -> {
            QueueArray<Integer> q = new QueueArray<>(n);
            for (int i = 0; i < n; i++)
                q.enqueue(i);
        }, new QueueArray<Integer>(n).usoMemoria()));

        // Queue Array - atender (dequeue)
        res.add(medir("E2_QueueArray", "atender", n, () -> {
            QueueArray<Integer> q = new QueueArray<>(n);
            for (int i = 0; i < n; i++)
                q.enqueue(i);
            for (int i = 0; i < n; i++)
                q.dequeue();
        }, new QueueArray<Integer>(n).usoMemoria()));

        // Queue Lista - encolar
        res.add(medir("E2_QueueLista", "encolar", n, () -> {
            QueueLista<Integer> q = new QueueLista<>();
            for (int i = 0; i < n; i++)
                q.enqueue(i);
        }, new QueueLista<Integer>().usoMemoria()));

        // Queue Lista - atender
        res.add(medir("E2_QueueLista", "atender", n, () -> {
            QueueLista<Integer> q = new QueueLista<>();
            for (int i = 0; i < n; i++)
                q.enqueue(i);
            for (int i = 0; i < n; i++)
                q.dequeue();
        }, new QueueLista<Integer>().usoMemoria()));

        // Stack Array - encolar (incorrecto para FIFO)
        res.add(medir("E2_StackArray", "encolar", n, () -> {
            StackArray<Integer> s = new StackArray<>(n);
            for (int i = 0; i < n; i++)
                s.push(i);
        }, new StackArray<Integer>(n).usoMemoria()));

        // Stack Array - "atender" (pop - orden incorrecto)
        res.add(medir("E2_StackArray", "atender", n, () -> {
            StackArray<Integer> s = new StackArray<>(n);
            for (int i = 0; i < n; i++)
                s.push(i);
            for (int i = 0; i < n; i++)
                s.pop();
        }, new StackArray<Integer>(n).usoMemoria()));

        // Stack Lista - encolar
        res.add(medir("E2_StackLista", "encolar", n, () -> {
            StackLista<Integer> s = new StackLista<>();
            for (int i = 0; i < n; i++)
                s.push(i);
        }, new StackLista<Integer>().usoMemoria()));

        // Stack Lista - "atender"
        res.add(medir("E2_StackLista", "atender", n, () -> {
            StackLista<Integer> s = new StackLista<>();
            for (int i = 0; i < n; i++)
                s.push(i);
            for (int i = 0; i < n; i++)
                s.pop();
        }, new StackLista<Integer>().usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────

    private static ResultadoBenchmark medir(String estructura, String operacion, int tamanio,
            Runnable tarea, long memoriaBytes) {
        long inicio = System.nanoTime();
        tarea.run();
        long tiempo = System.nanoTime() - inicio;
        System.out.printf("  [%s] %s n=%d => %d ns%n", estructura, operacion, tamanio, tiempo);
        return new ResultadoBenchmark(estructura, operacion, tamanio, tiempo, memoriaBytes);
    }

    private static void exportarJSON(List<ResultadoBenchmark> resultados, String ruta) {
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("[\n");
            for (int i = 0; i < resultados.size(); i++) {
                writer.write("  " + resultados.get(i).toJson());
                if (i < resultados.size() - 1)
                    writer.write(",");
                writer.write("\n");
            }
            writer.write("]\n");
            System.out.println("JSON exportado: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al exportar JSON: " + e.getMessage());
        }
    }
}