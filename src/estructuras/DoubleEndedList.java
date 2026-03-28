package estructuras;

/**
 * Double Ended List (Deque): permite inserción y eliminación en ambos extremos eficientemente.
 * Internamente usa una lista doblemente enlazada.
 */
public class DoubleEndedList<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo<T> anterior;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public DoubleEndedList() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    // Inserción al inicio (O(1))
    public void insertarAlInicio(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        }
        tamanio++;
    }

    // Inserción al final (O(1))
    public void insertarAlFinal(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (cola == null) {
            cabeza = cola = nuevo;
        } else {
            nuevo.anterior = cola;
            cola.siguiente = nuevo;
            cola = nuevo;
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
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        Nodo<T> prev = actual.anterior;
        prev.siguiente = nuevo;
        nuevo.anterior = prev;
        nuevo.siguiente = actual;
        actual.anterior = nuevo;
        tamanio++;
    }

    // Eliminar al inicio (O(1))
    public T eliminarAlInicio() {
        if (cabeza == null) throw new RuntimeException("Lista vacía");
        T dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        if (cabeza != null) cabeza.anterior = null; else cola = null;
        tamanio--;
        return dato;
    }

    // Eliminar al final (O(1))
    public T eliminarAlFinal() {
        if (cola == null) throw new RuntimeException("Lista vacía");
        T dato = cola.dato;
        cola = cola.anterior;
        if (cola != null) cola.siguiente = null; else cabeza = null;
        tamanio--;
        return dato;
    }

    // Eliminación por posición
    public void eliminar(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        if (posicion == 0) { eliminarAlInicio(); return; }
        if (posicion == tamanio - 1) { eliminarAlFinal(); return; }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        actual.anterior.siguiente = actual.siguiente;
        actual.siguiente.anterior = actual.anterior;
        tamanio--;
    }

    // Búsqueda
    public int buscar(T valor) {
        Nodo<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (actual.dato.equals(valor)) return indice;
            actual = actual.siguiente;
            indice++;
        }
        return -1;
    }

    // Acceso por índice
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        Nodo<T> actual;
        if (posicion < tamanio / 2) {
            actual = cabeza;
            for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        } else {
            actual = cola;
            for (int i = tamanio - 1; i > posicion; i--) actual = actual.anterior;
        }
        return actual.dato;
    }

    // Reemplazo
    public void reemplazar(int posicion, T nuevoValor) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        actual.dato = nuevoValor;
    }

    public T verInicio() { return cabeza != null ? cabeza.dato : null; }
    public T verFinal()  { return cola != null ? cola.dato : null; }

    public long usoMemoria() { return tamanio * 28L; }
    public int getTamanio()  { return tamanio; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" <-> ");
            actual = actual.siguiente;
        }
        return sb.append("]").toString();
    }
}