package estructuras;

/**
 * Stack (Pila) implementado con un arreglo dinámico.
 * Política LIFO: Last In, First Out.
 */
public class StackArray<T> {

    private Object[] datos;
    private int tope;
    private int capacidad;

    public StackArray(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.datos = new Object[capacidad];
        this.tope = -1;
    }

    // Push: insertar en el tope
    public void push(T valor) {
        if (tope == capacidad - 1) {
            capacidad *= 2;
            Object[] nuevo = new Object[capacidad];
            System.arraycopy(datos, 0, nuevo, 0, tope + 1);
            datos = nuevo;
        }
        datos[++tope] = valor;
    }

    // Pop: extraer del tope
    @SuppressWarnings("unchecked")
    public T pop() {
        if (estaVacio()) throw new RuntimeException("Stack vacío");
        T valor = (T) datos[tope];
        datos[tope--] = null;
        return valor;
    }

    // Peek: ver el tope sin extraer
    @SuppressWarnings("unchecked")
    public T peek() {
        if (estaVacio()) throw new RuntimeException("Stack vacío");
        return (T) datos[tope];
    }

    public boolean estaVacio() { return tope == -1; }
    public int getTamanio()    { return tope + 1; }
    public long usoMemoria()   { return 16 + (capacidad * 4L); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tope -> [");
        for (int i = tope; i >= 0; i--) {
            sb.append(datos[i]);
            if (i > 0) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}