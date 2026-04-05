package benchmark;

import estructuras.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Ejecuta las pruebas de carga para todas las estructuras.
 * Corre cada experimento REPETICIONES veces y calcula promedios.
 */
public class Benchmark {

    private static final int REPETICIONES = 5;
    private static final int[] TAMANIOS = {10, 100, 1000, 10000};

    public static void main(String[] args) {
        new File("resultados").mkdir(); // Crear carpeta de resultados si no existe

        for (int corrida = 1; corrida <= REPETICIONES; corrida++) {
            System.out.println("=== Corrida " + corrida + " ===");
            List<ResultadoBenchmark> resultados = new ArrayList<>();

            for (int n : TAMANIOS) {
                resultados.addAll(benchmarkArray(n));
                resultados.addAll(benchmarkListaSimple(n));
                resultados.addAll(benchmarkListaDoble(n));
                resultados.addAll(benchmarkDoubleEndedList(n));
                resultados.addAll(benchmarkListaCircular(n));
                resultados.addAll(benchmarkStackArray(n));
                resultados.addAll(benchmarkStackLista(n));
                resultados.addAll(benchmarkQueueArray(n));
                resultados.addAll(benchmarkQueueLista(n));
            }

            ExportadorJSON.exportar(resultados, "resultados/corrida_" + corrida + ".json");
        }

        System.out.println("\n=== Benchmark completado. Archivos JSON en /resultados ===");
    }

    // ─────────────────────────────────────────────
    // ARRAY
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkArray(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        MiArray<Integer> arr = new MiArray<>(n);

        // Precargar
        for (int i = 0; i < n; i++) arr.insertarAlFinal(i);

        res.add(medir("Array", "insercion_inicio",    n, () -> { MiArray<Integer> a = new MiArray<>(n); for(int i=0;i<n;i++) a.insertarAlInicio(i); }, arr.usoMemoria()));
        res.add(medir("Array", "insercion_final",     n, () -> { MiArray<Integer> a = new MiArray<>(n); for(int i=0;i<n;i++) a.insertarAlFinal(i); },  arr.usoMemoria()));
        res.add(medir("Array", "insercion_intermedia",n, () -> { MiArray<Integer> a = new MiArray<>(n); for(int i=0;i<n;i++) a.insertarAlFinal(i); a.insertarEnPosicion(n/2, 99); }, arr.usoMemoria()));
        res.add(medir("Array", "busqueda",            n, () -> { for(int i=0;i<n;i++) arr.buscar(n/2); }, arr.usoMemoria()));
        res.add(medir("Array", "acceso_indice",       n, () -> { for(int i=0;i<n;i++) arr.obtener(i); }, arr.usoMemoria()));
        res.add(medir("Array", "reemplazo",           n, () -> { for(int i=0;i<n;i++) arr.reemplazar(i, i*2); }, arr.usoMemoria()));
        res.add(medir("Array", "eliminacion",         n, () -> { MiArray<Integer> a = new MiArray<>(n); for(int i=0;i<n;i++) a.insertarAlFinal(i); for(int i=0;i<n;i++) a.eliminar(0); }, arr.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // LISTA SIMPLE
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkListaSimple(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        ListaSimple<Integer> ref = new ListaSimple<>();
        for (int i = 0; i < n; i++) ref.insertarAlFinal(i);

        res.add(medir("ListaSimple", "insercion_inicio",     n, () -> { ListaSimple<Integer> l = new ListaSimple<>(); for(int i=0;i<n;i++) l.insertarAlInicio(i); }, ref.usoMemoria()));
        res.add(medir("ListaSimple", "insercion_final",      n, () -> { ListaSimple<Integer> l = new ListaSimple<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); },  ref.usoMemoria()));
        res.add(medir("ListaSimple", "insercion_intermedia", n, () -> { ListaSimple<Integer> l = new ListaSimple<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); l.insertarEnPosicion(n/2, 99); }, ref.usoMemoria()));
        res.add(medir("ListaSimple", "busqueda",             n, () -> { for(int i=0;i<n;i++) ref.buscar(n/2); }, ref.usoMemoria()));
        res.add(medir("ListaSimple", "acceso_indice",        n, () -> { for(int i=0;i<n;i++) ref.obtener(i); }, ref.usoMemoria()));
        res.add(medir("ListaSimple", "reemplazo",            n, () -> { for(int i=0;i<n;i++) ref.reemplazar(i, i*2); }, ref.usoMemoria()));
        res.add(medir("ListaSimple", "eliminacion",          n, () -> { ListaSimple<Integer> l = new ListaSimple<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); for(int i=0;i<n;i++) l.eliminar(0); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // LISTA DOBLE
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkListaDoble(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        ListaDoble<Integer> ref = new ListaDoble<>();
        for (int i = 0; i < n; i++) ref.insertarAlFinal(i);

        res.add(medir("ListaDoble", "insercion_inicio",     n, () -> { ListaDoble<Integer> l = new ListaDoble<>(); for(int i=0;i<n;i++) l.insertarAlInicio(i); }, ref.usoMemoria()));
        res.add(medir("ListaDoble", "insercion_final",      n, () -> { ListaDoble<Integer> l = new ListaDoble<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); },  ref.usoMemoria()));
        res.add(medir("ListaDoble", "insercion_intermedia", n, () -> { ListaDoble<Integer> l = new ListaDoble<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); l.insertarEnPosicion(n/2, 99); }, ref.usoMemoria()));
        res.add(medir("ListaDoble", "busqueda",             n, () -> { for(int i=0;i<n;i++) ref.buscar(n/2); }, ref.usoMemoria()));
        res.add(medir("ListaDoble", "acceso_indice",        n, () -> { for(int i=0;i<n;i++) ref.obtener(i); }, ref.usoMemoria()));
        res.add(medir("ListaDoble", "reemplazo",            n, () -> { for(int i=0;i<n;i++) ref.reemplazar(i, i*2); }, ref.usoMemoria()));
        res.add(medir("ListaDoble", "eliminacion",          n, () -> { ListaDoble<Integer> l = new ListaDoble<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); for(int i=0;i<n;i++) l.eliminar(0); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // DOUBLE ENDED LIST
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkDoubleEndedList(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        DoubleEndedList<Integer> ref = new DoubleEndedList<>();
        for (int i = 0; i < n; i++) ref.insertarAlFinal(i);

        res.add(medir("DoubleEndedList", "insercion_inicio",     n, () -> { DoubleEndedList<Integer> l = new DoubleEndedList<>(); for(int i=0;i<n;i++) l.insertarAlInicio(i); }, ref.usoMemoria()));
        res.add(medir("DoubleEndedList", "insercion_final",      n, () -> { DoubleEndedList<Integer> l = new DoubleEndedList<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); },  ref.usoMemoria()));
        res.add(medir("DoubleEndedList", "insercion_intermedia", n, () -> { DoubleEndedList<Integer> l = new DoubleEndedList<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); l.insertarEnPosicion(n/2, 99); }, ref.usoMemoria()));
        res.add(medir("DoubleEndedList", "busqueda",             n, () -> { for(int i=0;i<n;i++) ref.buscar(n/2); }, ref.usoMemoria()));
        res.add(medir("DoubleEndedList", "acceso_indice",        n, () -> { for(int i=0;i<n;i++) ref.obtener(i); }, ref.usoMemoria()));
        res.add(medir("DoubleEndedList", "reemplazo",            n, () -> { for(int i=0;i<n;i++) ref.reemplazar(i, i*2); }, ref.usoMemoria()));
        res.add(medir("DoubleEndedList", "eliminacion",          n, () -> { DoubleEndedList<Integer> l = new DoubleEndedList<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); for(int i=0;i<n;i++) l.eliminarAlInicio(); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // LISTA CIRCULAR
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkListaCircular(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        ListaCircular<Integer> ref = new ListaCircular<>();
        for (int i = 0; i < n; i++) ref.insertarAlFinal(i);

        res.add(medir("ListaCircular", "insercion_inicio",     n, () -> { ListaCircular<Integer> l = new ListaCircular<>(); for(int i=0;i<n;i++) l.insertarAlInicio(i); }, ref.usoMemoria()));
        res.add(medir("ListaCircular", "insercion_final",      n, () -> { ListaCircular<Integer> l = new ListaCircular<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); },  ref.usoMemoria()));
        res.add(medir("ListaCircular", "insercion_intermedia", n, () -> { ListaCircular<Integer> l = new ListaCircular<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); l.insertarEnPosicion(n/2, 99); }, ref.usoMemoria()));
        res.add(medir("ListaCircular", "busqueda",             n, () -> { for(int i=0;i<n;i++) ref.buscar(n/2); }, ref.usoMemoria()));
        res.add(medir("ListaCircular", "acceso_indice",        n, () -> { for(int i=0;i<n;i++) ref.obtener(i); }, ref.usoMemoria()));
        res.add(medir("ListaCircular", "reemplazo",            n, () -> { for(int i=0;i<n;i++) ref.reemplazar(i, i*2); }, ref.usoMemoria()));
        res.add(medir("ListaCircular", "eliminacion",          n, () -> { ListaCircular<Integer> l = new ListaCircular<>(); for(int i=0;i<n;i++) l.insertarAlFinal(i); for(int i=0;i<n;i++) l.eliminar(0); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // STACK ARRAY
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkStackArray(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        StackArray<Integer> ref = new StackArray<>(n);
        for (int i = 0; i < n; i++) ref.push(i);

        res.add(medir("StackArray", "push", n, () -> { StackArray<Integer> s = new StackArray<>(n); for(int i=0;i<n;i++) s.push(i); }, ref.usoMemoria()));
        res.add(medir("StackArray", "pop",  n, () -> { StackArray<Integer> s = new StackArray<>(n); for(int i=0;i<n;i++) s.push(i); for(int i=0;i<n;i++) s.pop(); }, ref.usoMemoria()));
        res.add(medir("StackArray", "peek", n, () -> { for(int i=0;i<n;i++) ref.peek(); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // STACK LISTA
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkStackLista(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        StackLista<Integer> ref = new StackLista<>();
        for (int i = 0; i < n; i++) ref.push(i);

        res.add(medir("StackLista", "push", n, () -> { StackLista<Integer> s = new StackLista<>(); for(int i=0;i<n;i++) s.push(i); }, ref.usoMemoria()));
        res.add(medir("StackLista", "pop",  n, () -> { StackLista<Integer> s = new StackLista<>(); for(int i=0;i<n;i++) s.push(i); for(int i=0;i<n;i++) s.pop(); }, ref.usoMemoria()));
        res.add(medir("StackLista", "peek", n, () -> { for(int i=0;i<n;i++) ref.peek(); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // QUEUE ARRAY
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkQueueArray(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        QueueArray<Integer> ref = new QueueArray<>(n);
        for (int i = 0; i < n; i++) ref.enqueue(i);

        res.add(medir("QueueArray", "enqueue", n, () -> { QueueArray<Integer> q = new QueueArray<>(n); for(int i=0;i<n;i++) q.enqueue(i); }, ref.usoMemoria()));
        res.add(medir("QueueArray", "dequeue", n, () -> { QueueArray<Integer> q = new QueueArray<>(n); for(int i=0;i<n;i++) q.enqueue(i); for(int i=0;i<n;i++) q.dequeue(); }, ref.usoMemoria()));
        res.add(medir("QueueArray", "peek",    n, () -> { for(int i=0;i<n;i++) ref.peek(); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // QUEUE LISTA
    // ─────────────────────────────────────────────
    private static List<ResultadoBenchmark> benchmarkQueueLista(int n) {
        List<ResultadoBenchmark> res = new ArrayList<>();
        QueueLista<Integer> ref = new QueueLista<>();
        for (int i = 0; i < n; i++) ref.enqueue(i);

        res.add(medir("QueueLista", "enqueue", n, () -> { QueueLista<Integer> q = new QueueLista<>(); for(int i=0;i<n;i++) q.enqueue(i); }, ref.usoMemoria()));
        res.add(medir("QueueLista", "dequeue", n, () -> { QueueLista<Integer> q = new QueueLista<>(); for(int i=0;i<n;i++) q.enqueue(i); for(int i=0;i<n;i++) q.dequeue(); }, ref.usoMemoria()));
        res.add(medir("QueueLista", "peek",    n, () -> { for(int i=0;i<n;i++) ref.peek(); }, ref.usoMemoria()));

        return res;
    }

    // ─────────────────────────────────────────────
    // MÉTODO AUXILIAR: medir tiempo de una operación
    // ─────────────────────────────────────────────
    private static ResultadoBenchmark medir(String estructura, String operacion, int tamanio,
                                             Runnable tarea, long memoriaBytes) {
        long inicio = System.nanoTime();
        tarea.run();
        long tiempo = System.nanoTime() - inicio;

        System.out.printf("  [%s] %s n=%d => %d ns%n", estructura, operacion, tamanio, tiempo);
        return new ResultadoBenchmark(estructura, operacion, tamanio, tiempo, memoriaBytes);
    }
}