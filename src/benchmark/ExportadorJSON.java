package benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exporta resultados de benchmark a archivos JSON.
 */
public class ExportadorJSON {

    /**
     * Guarda una lista de ResultadoBenchmark en un archivo JSON.
     * @param resultados Lista de resultados
     * @param ruta       Ruta del archivo destino (ej: "resultados/corrida_1.json")
     */
    public static void exportar(List<ResultadoBenchmark> resultados, String ruta) {
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("[\n");
            for (int i = 0; i < resultados.size(); i++) {
                writer.write("  " + resultados.get(i).toJson());
                if (i < resultados.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]\n");
            System.out.println("JSON exportado: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al exportar JSON: " + e.getMessage());
        }
    }
}