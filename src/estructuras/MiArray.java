package estructuras;

/**
 * Implementación de un arreglo dinámico genérico.
 */
public class MiArray<T> {

    private Object[] datos;
    private int tamanio;
    private int capacidad;

    public MiArray(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.datos = new Object[capacidad];
        this.tamanio = 0;
    }

    // Inserción al inicio
    public void insertarAlInicio(T valor) {
        asegurarCapacidad();
        for (int i = tamanio; i > 0; i--) {
            datos[i] = datos[i - 1];
        }
        datos[0] = valor;
        tamanio++;
    }

    // Inserción al final
    public void insertarAlFinal(T valor) {
        asegurarCapacidad();
        datos[tamanio] = valor;
        tamanio++;
    }

    // Inserción en posición intermedia
    public void insertarEnPosicion(int posicion, T valor) {
        if (posicion < 0 || posicion > tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        asegurarCapacidad();
        for (int i = tamanio; i > posicion; i--) {
            datos[i] = datos[i - 1];
        }
        datos[posicion] = valor;
        tamanio++;
    }

    // Eliminación por índice
    public void eliminar(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        for (int i = posicion; i < tamanio - 1; i++) {
            datos[i] = datos[i + 1];
        }
        datos[tamanio - 1] = null;
        tamanio--;
    }

    // Búsqueda lineal, retorna índice o -1
    public int buscar(T valor) {
        for (int i = 0; i < tamanio; i++) {
            if (datos[i].equals(valor)) return i;
        }
        return -1;
    }

    // Acceso por índice
    @SuppressWarnings("unchecked")
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        return (T) datos[posicion];
    }

    // Reemplazo de un dato
    public void reemplazar(int posicion, T nuevoValor) {
        if (posicion < 0 || posicion >= tamanio) throw new IndexOutOfBoundsException("Posición inválida");
        datos[posicion] = nuevoValor;
    }

    // Uso de memoria estimado en bytes
    public long usoMemoria() {
        // Referencia de objeto (16 bytes cabecera) + 4 bytes por referencia en el arreglo
        return 16 + (capacidad * 4L);
    }

    // Copia profunda del arreglo
    public MiArray<T> copy() {
        MiArray<T> nueva = new MiArray<>(capacidad);
        for (int i = 0; i < tamanio; i++) {
            nueva.insertarAlFinal((T) datos[i]);
        }
        return nueva;
    }

    public int getTamanio() { return tamanio; }

    private void asegurarCapacidad() {
        if (tamanio == capacidad) {
            capacidad *= 2;
            Object[] nuevo = new Object[capacidad];
            System.arraycopy(datos, 0, nuevo, 0, tamanio);
            datos = nuevo;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanio; i++) {
            sb.append(datos[i]);
            if (i < tamanio - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}