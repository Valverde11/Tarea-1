package estructuras;

/**
 * Lista simplemente enlazada genérica.
 */
public class ListaSimple<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo<T> cabeza;
    private int tamanio;

    public ListaSimple() {
        cabeza = null;
        tamanio = 0;
    }

    // Inserción al inicio
    public void insertarAlInicio(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamanio++;
    }

    // Inserción al final
    public void insertarAlFinal(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    // Inserción en posición intermedia
    public void insertarEnPosicion(int posicion, T valor) {
        if (posicion < 0 || posicion > tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        if (posicion == 0) { insertarAlInicio(valor); return; }
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
        if (posicion == 0) { cabeza = cabeza.siguiente; tamanio--; return; }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion - 1; i++) actual = actual.siguiente;
        actual.siguiente = actual.siguiente.siguiente;
        tamanio--;
    }

    // Búsqueda, retorna índice o -1
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
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        return actual.dato;
    }

    // Reemplazo de un dato
    public void reemplazar(int posicion, T nuevoValor) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        actual.dato = nuevoValor;
    }

    // Uso de memoria estimado en bytes: cada nodo = 16 cabecera + 4 dato + 4 puntero
    public long usoMemoria() {
        return tamanio * 24L;
    }

    public int getTamanio() { return tamanio; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" -> ");
            actual = actual.siguiente;
        }
        return sb.append("]").toString();
    }
}