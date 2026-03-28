package estructuras;

/**
 * Queue (Cola) implementada con arreglo circular.
 * Política FIFO: First In, First Out.
 */
public class QueueArray<T> {

    private Object[] datos;
    private int frente;
    private int fin;
    private int tamanio;
    private int capacidad;

    public QueueArray(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.datos = new Object[capacidad];
        this.frente = 0;
        this.fin = 0;
        this.tamanio = 0;
    }

    // Enqueue: insertar al final (O(1) amortizado)
    public void enqueue(T valor) {
        if (tamanio == capacidad) redimensionar();
        datos[fin] = valor;
        fin = (fin + 1) % capacidad;
        tamanio++;
    }

    // Dequeue: extraer del frente (O(1))
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (estaVacia()) throw new RuntimeException("Queue vacía");
        T valor = (T) datos[frente];
        datos[frente] = null;
        frente = (frente + 1) % capacidad;
        tamanio--;
        return valor;
    }

    // Peek: ver el frente sin extraer
    @SuppressWarnings("unchecked")
    public T peek() {
        if (estaVacia()) throw new RuntimeException("Queue vacía");
        return (T) datos[frente];
    }

    private void redimensionar() {
        int nuevaCapacidad = capacidad * 2;
        Object[] nuevo = new Object[nuevaCapacidad];
        for (int i = 0; i < tamanio; i++) {
            nuevo[i] = datos[(frente + i) % capacidad];
        }
        datos = nuevo;
        frente = 0;
        fin = tamanio;
        capacidad = nuevaCapacidad;
    }

    public boolean estaVacia() { return tamanio == 0; }
    public int getTamanio()    { return tamanio; }
    public long usoMemoria()   { return 16 + (capacidad * 4L); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Frente -> [");
        for (int i = 0; i < tamanio; i++) {
            sb.append(datos[(frente + i) % capacidad]);
            if (i < tamanio - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}