package estructuras;

/**
 * Lista doblemente enlazada genérica.
 */
public class ListaDoble<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo<T> anterior;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
            this.anterior = null;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public ListaDoble() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    // Inserción al inicio
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

    // Inserción al final
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

    // Eliminación por posición
    public void eliminar(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        if (posicion == 0) {
            cabeza = cabeza.siguiente;
            if (cabeza != null) cabeza.anterior = null; else cola = null;
        } else if (posicion == tamanio - 1) {
            cola = cola.anterior;
            if (cola != null) cola.siguiente = null; else cabeza = null;
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < posicion; i++) actual = actual.siguiente;
            actual.anterior.siguiente = actual.siguiente;
            actual.siguiente.anterior = actual.anterior;
        }
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

    // Acceso por índice (optimizado: busca desde el extremo más cercano)
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

    // Uso de memoria: cada nodo = 16 cabecera + 4 dato + 4 siguiente + 4 anterior
    public long usoMemoria() {
        return tamanio * 28L;
    }

    // Copia profunda de la lista
    public ListaDoble<T> copy() {
        ListaDoble<T> nueva = new ListaDoble<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            nueva.insertarAlFinal(actual.dato);
            actual = actual.siguiente;
        }
        return nueva;
    }

    public int getTamanio() { return tamanio; }

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