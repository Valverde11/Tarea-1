package benchmark;

/**
 * Resultado de una sola ejecución de benchmark.
 */
public class ResultadoBenchmark {
    public String estructura;
    public String operacion;
    public int tamanio;
    public long tiempoNs;       // Tiempo en nanosegundos
    public long memoriaBytes;   // Memoria usada en bytes

    public ResultadoBenchmark(String estructura, String operacion, int tamanio,
                               long tiempoNs, long memoriaBytes) {
        this.estructura    = estructura;
        this.operacion     = operacion;
        this.tamanio       = tamanio;
        this.tiempoNs      = tiempoNs;
        this.memoriaBytes  = memoriaBytes;
    }

    /** Convierte este resultado a una línea JSON. */
    public String toJson() {
        return String.format(
            "{\"estructura\":\"%s\",\"operacion\":\"%s\",\"tamanio\":%d,\"tiempo_ns\":%d,\"memoria_bytes\":%d}",
            estructura, operacion, tamanio, tiempoNs, memoriaBytes
        );
    }
}