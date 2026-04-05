package estructuras;

/**
 * Lista circular simplemente enlazada genérica.
 * El último nodo apunta de regreso a la cabeza.
 */
public class ListaCircular<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola; // Referencia a la cola para inserción O(1) al final
    private int tamanio;

    public ListaCircular() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    // Inserción al inicio
    public void insertarAlInicio(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (cabeza == null) {
            cabeza = cola = nuevo;
            nuevo.siguiente = cabeza;
        } else {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            cola.siguiente = cabeza; // Mantener circularidad
        }
        tamanio++;
    }

    // Inserción al final
    public void insertarAlFinal(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (cabeza == null) {
            cabeza = cola = nuevo;
            nuevo.siguiente = cabeza;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
            cola.siguiente = cabeza; // Mantener circularidad
        }
        tamanio++;
    }

    // Inserción en posición intermedia
    public void insertarEnPosicion(int posicion, T valor) {
        if (posicion < 0 || posicion > tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        if (posicion == 0) { insertarAlInicio(valor); return; }
        if (posicion == tamanio) { insertarAlFinal(valor); return; }
        Nodo<T> nuevo = new Nodo<>(valor);
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion - 1; i++) actual = actual.siguiente;
        nuevo.siguiente = actual.siguiente;
        actual.siguiente = nuevo;
        tamanio++;
    }

    // Eliminación por posición
    public void eliminar(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        if (posicion == 0) {
            if (tamanio == 1) { cabeza = cola = null; }
            else { cabeza = cabeza.siguiente; cola.siguiente = cabeza; }
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < posicion - 1; i++) actual = actual.siguiente;
            if (posicion == tamanio - 1) cola = actual;
            actual.siguiente = actual.siguiente.siguiente;
        }
        tamanio--;
    }

    // Búsqueda
    public int buscar(T valor) {
        if (cabeza == null) return -1;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (actual.dato.equals(valor)) return i;
            actual = actual.siguiente;
        }
        return -1;
    }

    // Acceso por índice
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        return actual.dato;
    }

    // Reemplazo
    public void reemplazar(int posicion, T nuevoValor) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        actual.dato = nuevoValor;
    }

    public long usoMemoria() { return tamanio * 24L; }

    // Copia profunda de la lista
    public ListaCircular<T> copy() {
        ListaCircular<T> nueva = new ListaCircular<>();
        if (cabeza != null) {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < tamanio; i++) {
                nueva.insertarAlFinal(actual.dato);
                actual = actual.siguiente;
            }
        }
        return nueva;
    }

    public int getTamanio()  { return tamanio; }

    @Override
    public String toString() {
        if (cabeza == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            sb.append(actual.dato);
            if (i < tamanio - 1) sb.append(" -> ");
            actual = actual.siguiente;
        }
        return sb.append(" -> (cabeza)]").toString();
    }
}