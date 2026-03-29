package estructuras;

/**
 * Queue (Cola) implementada con lista enlazada.
 * Política FIFO: First In, First Out.
 */
public class QueueLista<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public QueueLista() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    // Enqueue: insertar al final (O(1))
    public void enqueue(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (fin == null) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    // Dequeue: extraer del frente (O(1))
    public T dequeue() {
        if (estaVacia()) throw new RuntimeException("Queue vacía");
        T valor = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return valor;
    }

    // Peek: ver el frente sin extraer
    public T peek() {
        if (estaVacia()) throw new RuntimeException("Queue vacía");
        return frente.dato;
    }

    public boolean estaVacia() { return frente == null; }
    public int getTamanio()    { return tamanio; }
    public long usoMemoria()   { return tamanio * 24L; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Frente -> [");
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        return sb.append("]").toString();
    }
}