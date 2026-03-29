package estructuras;

/**
 * Stack (Pila) implementado con lista enlazada.
 * Política LIFO: Last In, First Out.
 */
public class StackLista<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> tope;
    private int tamanio;

    public StackLista() {
        tope = null;
        tamanio = 0;
    }

    // Push: insertar en el tope (O(1))
    public void push(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }

    // Pop: extraer del tope (O(1))
    public T pop() {
        if (estaVacio()) throw new RuntimeException("Stack vacío");
        T valor = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return valor;
    }

    // Peek: ver el tope sin extraer (O(1))
    public T peek() {
        if (estaVacio()) throw new RuntimeException("Stack vacío");
        return tope.dato;
    }

    public boolean estaVacio() { return tope == null; }
    public int getTamanio()    { return tamanio; }
    public long usoMemoria()   { return tamanio * 24L; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tope -> [");
        Nodo<T> actual = tope;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        return sb.append("]").toString();
    }
}