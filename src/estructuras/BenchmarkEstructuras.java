package estructuras;

import java.util.Random;

/**
 * Benchmark comparativo de operaciones para estructuras de datos.
 */
public class BenchmarkEstructuras {

    private static final int[] TAMANOS = {10, 100, 1000, 10000};
    private static final int ITER = 100;
    private static final int ITER_BUSCAR = 50;

    public static void main(String[] args) {
        System.out.println("Benchmark de estructuras de datos\n");

        for (int tamanio : TAMANOS) {
            System.out.println("=== Tamaño: " + tamanio + " elementos ===");
            int[] baseData = crearDatosAleatorios(tamanio);

            correrBenchmark("MiArray", tamanio, baseData);
            correrBenchmark("ListaSimple", tamanio, baseData);
            correrBenchmark("ListaDoble", tamanio, baseData);
            correrBenchmark("DoubleEndedList", tamanio, baseData);
            correrBenchmark("ListaCircular", tamanio, baseData);
            System.out.println();
        }

        System.out.println("Benchmark completo finalizado.");
        System.out.println("Nota: los valores dependen del equipo, JVM y carga de CPU.");
    }

    private static void correrBenchmark(String nombre, int tamanio, int[] baseData) {
        System.out.println("--- " + nombre + " ---");

        // Crear lista prellenada en valores aleatorios (igual para todas las estructuras)
        Object listaPrelenada = crearListaPrelenada(nombre, tamanio, baseData);

        long tInsertInicio = promedioTiempo(() -> insertarAlInicio(nombre, listaPrelenada));
        System.out.printf("Insertar al inicio: %d ns (%.3f ms)\n", tInsertInicio, tInsertInicio / 1_000_000.0);

        long tInsertFinal = promedioTiempo(() -> insertarAlFinal(nombre, listaPrelenada));
        System.out.printf("Insertar al final: %d ns (%.3f ms)\n", tInsertFinal, tInsertFinal / 1_000_000.0);

        long tInsertMedio = promedioTiempo(() -> insertarEnMedio(nombre, listaPrelenada));
        System.out.printf("Insertar en medio: %d ns (%.3f ms)\n", tInsertMedio, tInsertMedio / 1_000_000.0);

        long tEliminar = promedioTiempo(() -> eliminarItems(nombre, listaPrelenada));
        System.out.printf("Eliminar: %d ns (%.3f ms)\n", tEliminar, tEliminar / 1_000_000.0);

        long tBuscar = promedioTiempo(() -> buscarItems(nombre, listaPrelenada));
        System.out.printf("Buscar: %d ns (%.3f ms)\n", tBuscar, tBuscar / 1_000_000.0);

        long tAcceso = promedioTiempo(() -> accesoPorIndice(nombre, listaPrelenada));
        System.out.printf("Acceso por índice: %d ns (%.3f ms)\n", tAcceso, tAcceso / 1_000_000.0);

        long tReemplazo = promedioTiempo(() -> reemplazo(nombre, listaPrelenada));
        System.out.printf("Reemplazo: %d ns (%.3f ms)\n", tReemplazo, tReemplazo / 1_000_000.0);

        long mem = usoMemoria(nombre, listaPrelenada);
        System.out.printf("Uso de memoria estimado: %d bytes\n", mem);

        System.out.println();
    }

    private static long tiempo(Runnable operacion) {
        long inicio = System.nanoTime();
        operacion.run();
        long fin = System.nanoTime();
        return fin - inicio;
    }

    private static long promedioTiempo(Runnable operacion) {
        final int REP = 8;
        long total = 0;
        for (int i = 0; i < REP; i++) {
            // Recalentar un poco la JVM en las primeras iteraciones
            if (i == 2) System.gc();
            total += tiempo(operacion);
        }
        return total / REP;
    }

    private static int[] crearDatosAleatorios(int tamanio) {
        int[] datos = new int[tamanio];
        Random rnd = new Random(123456);
        for (int i = 0; i < tamanio; i++) {
            datos[i] = rnd.nextInt(1_000_000);
        }
        return datos;
    }

    private static Object crearListaPrelenada(String nombre, int tamanio, int[] baseData) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = new MiArray<>(tamanio + ITER);
                for (int i = 0; i < tamanio; i++) s.insertarAlFinal(baseData[i]);
                return s;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = new ListaSimple<>();
                for (int i = 0; i < tamanio; i++) s.insertarAlFinal(baseData[i]);
                return s;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = new ListaDoble<>();
                for (int i = 0; i < tamanio; i++) s.insertarAlFinal(baseData[i]);
                return s;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = new DoubleEndedList<>();
                for (int i = 0; i < tamanio; i++) s.insertarAlFinal(baseData[i]);
                return s;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = new ListaCircular<>();
                for (int i = 0; i < tamanio; i++) s.insertarAlFinal(baseData[i]);
                return s;
            }
            default:
                return null;
        }
    }

    private static void insertarAlInicio(String nombre, Object listaPrelenada) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = ((MiArray<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlInicio(i);
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = ((ListaSimple<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlInicio(i);
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = ((ListaDoble<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlInicio(i);
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = ((ListaCircular<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlInicio(i);
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = ((DoubleEndedList<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlInicio(i);
                break;
            }
        }
    }

    private static void insertarAlFinal(String nombre, Object listaPrelenada) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = ((MiArray<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlFinal(i);
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = ((ListaSimple<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlFinal(i);
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = ((ListaDoble<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlFinal(i);
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = ((ListaCircular<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlFinal(i);
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = ((DoubleEndedList<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarAlFinal(i);
                break;
            }
        }
    }

    private static void insertarEnMedio(String nombre, Object listaPrelenada) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = ((MiArray<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarEnPosicion(s.getTamanio() / 2, i);
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = ((ListaSimple<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarEnPosicion(s.getTamanio() / 2, i);
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = ((ListaDoble<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarEnPosicion(s.getTamanio() / 2, i);
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = ((ListaCircular<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarEnPosicion(s.getTamanio() / 2, i);
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = ((DoubleEndedList<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER; i++) s.insertarEnPosicion(s.getTamanio() / 2, i);
                break;
            }
        }
    }

    private static void eliminarItems(String nombre, Object listaPrelenada) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = ((MiArray<Integer>) listaPrelenada).copy();
                int maxEliminar = Math.min(ITER, s.getTamanio());
                for (int i = 0; i < maxEliminar; i++) s.eliminar(0);
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = ((ListaSimple<Integer>) listaPrelenada).copy();
                int maxEliminar = Math.min(ITER, s.getTamanio());
                for (int i = 0; i < maxEliminar; i++) s.eliminar(0);
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = ((ListaDoble<Integer>) listaPrelenada).copy();
                int maxEliminar = Math.min(ITER, s.getTamanio());
                for (int i = 0; i < maxEliminar; i++) s.eliminar(0);
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = ((ListaCircular<Integer>) listaPrelenada).copy();
                int maxEliminar = Math.min(ITER, s.getTamanio());
                for (int i = 0; i < maxEliminar; i++) s.eliminar(0);
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = ((DoubleEndedList<Integer>) listaPrelenada).copy();
                int maxEliminar = Math.min(ITER, s.getTamanio());
                for (int i = 0; i < maxEliminar; i++) s.eliminar(0);
                break;
            }
        }
    }

    private static void buscarItems(String nombre, Object listaPrelenada) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = (MiArray<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(s.getTamanio() - 1);
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(-1);
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = (ListaSimple<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(s.getTamanio() - 1);
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(-1);
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = (ListaDoble<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(s.getTamanio() - 1);
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(-1);
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = (ListaCircular<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(s.getTamanio() - 1);
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(-1);
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = (DoubleEndedList<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(s.getTamanio() - 1);
                for (int i = 0; i < ITER_BUSCAR; i++) s.buscar(-1);
                break;
            }
        }
    }

    private static void accesoPorIndice(String nombre, Object listaPrelenada) {
        Random rand = new Random(1234);
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = (MiArray<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.obtener(rand.nextInt(s.getTamanio()));
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = (ListaSimple<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.obtener(rand.nextInt(s.getTamanio()));
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = (ListaDoble<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.obtener(rand.nextInt(s.getTamanio()));
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = (ListaCircular<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.obtener(rand.nextInt(s.getTamanio()));
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = (DoubleEndedList<Integer>) listaPrelenada;
                for (int i = 0; i < ITER_BUSCAR; i++) s.obtener(rand.nextInt(s.getTamanio()));
                break;
            }
        }
    }

    private static void reemplazo(String nombre, Object listaPrelenada) {
        Random rand = new Random(5678);
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = ((MiArray<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER_BUSCAR; i++) s.reemplazar(rand.nextInt(s.getTamanio()), -i);
                break;
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = ((ListaSimple<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER_BUSCAR; i++) s.reemplazar(rand.nextInt(s.getTamanio()), -i);
                break;
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = ((ListaDoble<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER_BUSCAR; i++) s.reemplazar(rand.nextInt(s.getTamanio()), -i);
                break;
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = ((ListaCircular<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER_BUSCAR; i++) s.reemplazar(rand.nextInt(s.getTamanio()), -i);
                break;
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = ((DoubleEndedList<Integer>) listaPrelenada).copy();
                for (int i = 0; i < ITER_BUSCAR; i++) s.reemplazar(rand.nextInt(s.getTamanio()), -i);
                break;
            }
        }
    }

    private static long usoMemoria(String nombre, Object listaPrelenada) {
        switch (nombre) {
            case "MiArray": {
                MiArray<Integer> s = (MiArray<Integer>) listaPrelenada;
                return s.usoMemoria();
            }
            case "ListaSimple": {
                ListaSimple<Integer> s = (ListaSimple<Integer>) listaPrelenada;
                return s.usoMemoria();
            }
            case "ListaDoble": {
                ListaDoble<Integer> s = (ListaDoble<Integer>) listaPrelenada;
                return s.usoMemoria();
            }
            case "ListaCircular": {
                ListaCircular<Integer> s = (ListaCircular<Integer>) listaPrelenada;
                return s.usoMemoria();
            }
            case "DoubleEndedList": {
                DoubleEndedList<Integer> s = (DoubleEndedList<Integer>) listaPrelenada;
                return s.usoMemoria();
            }
            default:
                return 0;
        }
    }
}
