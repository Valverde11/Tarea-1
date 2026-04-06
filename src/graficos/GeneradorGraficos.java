package graficos;

import benchmark.ResultadoBenchmark;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Genera gráficos de barras y líneas para las métricas del benchmark.
 * Usa Java2D (sin librerías externas).
 */
public class GeneradorGraficos {

    // Colores por estructura
    private static final Map<String, Color> COLORES = new LinkedHashMap<>();
    static {
        COLORES.put("Array", new Color(70, 130, 180));
        COLORES.put("ListaSimple", new Color(60, 179, 113));
        COLORES.put("ListaDoble", new Color(255, 140, 0));
        COLORES.put("DoubleEndedList", new Color(220, 20, 60));
        COLORES.put("ListaCircular", new Color(147, 112, 219));
        COLORES.put("StackArray", new Color(255, 215, 0));
        COLORES.put("StackLista", new Color(64, 224, 208));
        COLORES.put("QueueArray", new Color(255, 105, 180));
        COLORES.put("QueueLista", new Color(139, 69, 19));
    }

    private static final int ANCHO = 900;
    private static final int ALTO = 550;
    private static final int MARGEN_I = 90;
    private static final int MARGEN_D = 30;
    private static final int MARGEN_S = 50;
    private static final int MARGEN_B = 120;

    public static void main(String[] args) throws Exception {
        new File("graficos").mkdir();

        // Cargar todos los JSON de resultados y calcular promedios
        Map<String, Map<String, Map<Integer, Long>>> promediosTiempo = new HashMap<>();
        Map<String, Map<Integer, Long>> promediosMemoria = new HashMap<>();
        cargarPromedios("resultados", promediosTiempo, promediosMemoria);

        int[] tamanios = { 10, 100, 1000, 10000 };

        // Gráfico 1: Tiempo vs N - Inserción al inicio (todas las estructuras)
        graficoLinea(promediosTiempo, "insercion_inicio", tamanios,
                "Tiempo de Inserción al Inicio vs N", "graficos/tiempo_insercion_inicio.png");

        // Gráfico 2: Tiempo vs N - Inserción al final
        graficoLinea(promediosTiempo, "insercion_final", tamanios,
                "Tiempo de Inserción al Final vs N", "graficos/tiempo_insercion_final.png");

        // Gráfico 3: Tiempo vs N - Búsqueda
        graficoLinea(promediosTiempo, "busqueda", tamanios,
                "Tiempo de Búsqueda vs N", "graficos/tiempo_busqueda.png");

        // Gráfico 4: Tiempo vs N - Acceso por índice
        graficoLinea(promediosTiempo, "acceso_indice", tamanios,
                "Tiempo de Acceso por Índice vs N", "graficos/tiempo_acceso_indice.png");

        // Gráfico 5: Tiempo vs N - Eliminación
        graficoLinea(promediosTiempo, "eliminacion", tamanios,
                "Tiempo de Eliminación vs N", "graficos/tiempo_eliminacion.png");

        // Gráfico 6: Tiempo vs N - Inserción intermedia
        graficoLinea(promediosTiempo, "insercion_intermedia", tamanios,
                "Tiempo de Inserción Intermedia vs N", "graficos/tiempo_insercion_intermedia.png");

        // Gráfico 7: Tiempo vs N - Reemplazo
        graficoLinea(promediosTiempo, "reemplazo", tamanios,
                "Tiempo de Reemplazo vs N", "graficos/tiempo_reemplazo.png");

        // Gráfico 8: Memoria vs N
        graficoMemoria(promediosMemoria, tamanios,
                "Uso de Memoria vs N", "graficos/memoria_vs_n.png");

        // Gráfico 9: Stack vs Queue - Comparación por operación (push/enqueue y
        // pop/dequeue)
        graficoStackVsQueue(promediosTiempo, tamanios,
                "Stack vs Queue: Comparación de Operaciones", "graficos/stack_vs_queue.png");

        // ── Gráficos de escenarios (leen escenarios_corrida_*.json) ──────
        Map<String, Map<String, Map<Integer, Long>>> promediosEsc = new HashMap<>();
        Map<String, Map<Integer, Long>> memoriaEsc = new HashMap<>();
        cargarPromedios("resultados", promediosEsc, memoriaEsc, "escenarios_corrida_");

        // Gráfico 10: Escenario 1 - Undo: tiempo de insertar
        graficoLinea(promediosEsc, "insertar", tamanios,
                "Escenario 1 (Undo): Tiempo de Inserción - Stack vs Queue",
                "graficos/esc1_insertar.png");

        // Gráfico 11: Escenario 1 - Undo: tiempo de deshacer
        graficoLinea(promediosEsc, "deshacer", tamanios,
                "Escenario 1 (Undo): Tiempo de Deshacer - Stack (correcto) vs Queue (incorrecto)",
                "graficos/esc1_deshacer.png");

        // Gráfico 12: Escenario 2 - FIFO: tiempo de encolar
        graficoLinea(promediosEsc, "encolar", tamanios,
                "Escenario 2 (FIFO): Tiempo de Encolar - Queue vs Stack",
                "graficos/esc2_encolar.png");

        // Gráfico 13: Escenario 2 - FIFO: tiempo de atender
        graficoLinea(promediosEsc, "atender", tamanios,
                "Escenario 2 (FIFO): Tiempo de Atender - Queue (correcto) vs Stack (incorrecto)",
                "graficos/esc2_atender.png");

        System.out.println("Gráficos generados en /graficos/");
    }

    /**
     * Carga los JSON de la carpeta resultados y calcula promedios.
     * Sobrecarga sin prefijo — lee todos los JSON de la carpeta.
     */
    private static void cargarPromedios(String carpeta,
            Map<String, Map<String, Map<Integer, Long>>> promediosTiempo,
            Map<String, Map<Integer, Long>> promediosMemoria) throws Exception {
        cargarPromedios(carpeta, promediosTiempo, promediosMemoria, "corrida_");
    }

    /**
     * Carga los JSON cuyo nombre empieza con el prefijo indicado.
     */
    private static void cargarPromedios(String carpeta,
            Map<String, Map<String, Map<Integer, Long>>> promediosTiempo,
            Map<String, Map<Integer, Long>> promediosMemoria,
            String prefijo) throws Exception {

        // Acumuladores: estructura -> operacion -> tamanio -> [suma, count]
        Map<String, Map<String, Map<Integer, long[]>>> acumT = new HashMap<>();
        Map<String, Map<Integer, long[]>> acumM = new HashMap<>();

        File dir = new File(carpeta);
        if (!dir.exists()) {
            System.err.println("No existe la carpeta " + carpeta);
            return;
        }

        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (!f.getName().endsWith(".json"))
                continue;
            if (!f.getName().startsWith(prefijo))
                continue;
            List<ResultadoBenchmark> lista = parsearJSON(f);
            for (ResultadoBenchmark r : lista) {
                // Tiempo
                acumT.computeIfAbsent(r.estructura, k -> new HashMap<>())
                        .computeIfAbsent(r.operacion, k -> new HashMap<>())
                        .computeIfAbsent(r.tamanio, k -> new long[] { 0, 0 });
                long[] t = acumT.get(r.estructura).get(r.operacion).get(r.tamanio);
                t[0] += r.tiempoNs;
                t[1]++;

                // Memoria
                acumM.computeIfAbsent(r.estructura, k -> new HashMap<>())
                        .computeIfAbsent(r.tamanio, k -> new long[] { 0, 0 });
                long[] m = acumM.get(r.estructura).get(r.tamanio);
                m[0] += r.memoriaBytes;
                m[1]++;
            }
        }

        // Calcular promedios
        for (var e : acumT.entrySet()) {
            for (var op : e.getValue().entrySet()) {
                for (var n : op.getValue().entrySet()) {
                    long[] v = n.getValue();
                    promediosTiempo.computeIfAbsent(e.getKey(), k -> new HashMap<>())
                            .computeIfAbsent(op.getKey(), k -> new HashMap<>())
                            .put(n.getKey(), v[1] > 0 ? v[0] / v[1] : 0);
                }
            }
        }
        for (var e : acumM.entrySet()) {
            for (var n : e.getValue().entrySet()) {
                long[] v = n.getValue();
                promediosMemoria.computeIfAbsent(e.getKey(), k -> new HashMap<>())
                        .put(n.getKey(), v[1] > 0 ? v[0] / v[1] : 0);
            }
        }
    }

    /**
     * Parser JSON muy simple (sin librerías externas).
     */
    private static List<ResultadoBenchmark> parsearJSON(File f) throws Exception {
        List<ResultadoBenchmark> lista = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null)
                sb.append(linea);
        }
        String contenido = sb.toString().trim();
        // Quitar corchetes externos
        contenido = contenido.substring(1, contenido.length() - 1);
        // Cada objeto JSON está entre { }
        String[] objetos = contenido.split("\\},\\s*\\{");
        for (String obj : objetos) {
            obj = obj.replaceAll("[\\{\\}]", "").trim();
            Map<String, String> campos = new HashMap<>();
            for (String par : obj.split(",")) {
                String[] kv = par.split(":", 2);
                if (kv.length == 2) {
                    String k = kv[0].trim().replace("\"", "");
                    String v = kv[1].trim().replace("\"", "");
                    campos.put(k, v);
                }
            }
            if (campos.containsKey("estructura")) {
                lista.add(new ResultadoBenchmark(
                        campos.get("estructura"),
                        campos.get("operacion"),
                        Integer.parseInt(campos.get("tamanio")),
                        Long.parseLong(campos.get("tiempo_ns")),
                        Long.parseLong(campos.get("memoria_bytes"))));
            }
        }
        return lista;
    }

    /**
     * Genera gráfico de líneas: Tiempo vs N para una operación.
     */
    private static void graficoLinea(Map<String, Map<String, Map<Integer, Long>>> datos,
            String operacion, int[] tamanios,
            String titulo, String archivo) throws Exception {
        BufferedImage img = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ANCHO, ALTO);

        // Calcular máximo
        long maxTiempo = 1;
        for (var e : datos.entrySet()) {
            Map<String, Map<Integer, Long>> ops = e.getValue();
            if (!ops.containsKey(operacion))
                continue;
            for (long v : ops.get(operacion).values())
                if (v > maxTiempo)
                    maxTiempo = v;
        }

        int areaAncho = ANCHO - MARGEN_I - MARGEN_D;
        int areaAlto = ALTO - MARGEN_S - MARGEN_B;

        dibujarEjes(g, areaAncho, areaAlto, tamanios, maxTiempo, "Tiempo (ns)");
        dibujarTitulo(g, titulo);

        // Líneas por estructura
        List<String> estructuras = new ArrayList<>(COLORES.keySet());
        for (String est : estructuras) {
            if (!datos.containsKey(est) || !datos.get(est).containsKey(operacion))
                continue;
            Map<Integer, Long> puntos = datos.get(est).get(operacion);
            Color color = COLORES.getOrDefault(est, Color.GRAY);
            g.setColor(color);
            g.setStroke(new BasicStroke(2.5f));

            int[] xs = new int[tamanios.length];
            int[] ys = new int[tamanios.length];
            for (int i = 0; i < tamanios.length; i++) {
                xs[i] = MARGEN_I + (int) ((double) i / (tamanios.length - 1) * areaAncho);
                long val = puntos.getOrDefault(tamanios[i], 0L);
                ys[i] = MARGEN_S + areaAlto - (int) ((double) val / maxTiempo * areaAlto);
            }
            for (int i = 0; i < xs.length - 1; i++) {
                g.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                g.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
            }
            g.fillOval(xs[xs.length - 1] - 4, ys[ys.length - 1] - 4, 8, 8);
        }

        dibujarLeyenda(g, estructuras, datos, operacion);
        g.dispose();
        ImageIO.write(img, "PNG", new File(archivo));
        System.out.println("Gráfico guardado: " + archivo);
    }

    /**
     * Genera gráfico de líneas: Memoria vs N.
     */
    private static void graficoMemoria(Map<String, Map<Integer, Long>> datos,
            int[] tamanios, String titulo, String archivo) throws Exception {
        BufferedImage img = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ANCHO, ALTO);

        long maxMem = 1;
        for (var e : datos.entrySet())
            for (long v : e.getValue().values())
                if (v > maxMem)
                    maxMem = v;

        int areaAncho = ANCHO - MARGEN_I - MARGEN_D;
        int areaAlto = ALTO - MARGEN_S - MARGEN_B;

        dibujarEjes(g, areaAncho, areaAlto, tamanios, maxMem, "Memoria (bytes)");
        dibujarTitulo(g, titulo);

        List<String> estructuras = new ArrayList<>(COLORES.keySet());
        for (String est : estructuras) {
            if (!datos.containsKey(est))
                continue;
            Color color = COLORES.getOrDefault(est, Color.GRAY);
            g.setColor(color);
            g.setStroke(new BasicStroke(2.5f));
            int[] xs = new int[tamanios.length];
            int[] ys = new int[tamanios.length];
            for (int i = 0; i < tamanios.length; i++) {
                xs[i] = MARGEN_I + (int) ((double) i / (tamanios.length - 1) * areaAncho);
                long val = datos.get(est).getOrDefault(tamanios[i], 0L);
                ys[i] = MARGEN_S + areaAlto - (int) ((double) val / maxMem * areaAlto);
            }
            for (int i = 0; i < xs.length - 1; i++) {
                g.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                g.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
            }
            g.fillOval(xs[xs.length - 1] - 4, ys[ys.length - 1] - 4, 8, 8);
        }

        dibujarLeyendaMemoria(g, estructuras, datos);
        g.dispose();
        ImageIO.write(img, "PNG", new File(archivo));
        System.out.println("Gráfico guardado: " + archivo);
    }

    /**
     * Gráfico comparativo Stack (array y lista) vs Queue (array y lista)
     * para las operaciones push/enqueue y pop/dequeue.
     */
    private static void graficoStackVsQueue(Map<String, Map<String, Map<Integer, Long>>> datos,
            int[] tamanios, String titulo, String archivo) throws Exception {
        // Estructuras y operaciones a comparar
        String[][] pares = {
                { "StackArray", "push" },
                { "StackLista", "push" },
                { "QueueArray", "enqueue" },
                { "QueueLista", "enqueue" },
                { "StackArray", "pop" },
                { "StackLista", "pop" },
                { "QueueArray", "dequeue" },
                { "QueueLista", "dequeue" },
        };

        BufferedImage img = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ANCHO, ALTO);

        long maxTiempo = 1;
        for (String[] par : pares) {
            String est = par[0], op = par[1];
            if (datos.containsKey(est) && datos.get(est).containsKey(op)) {
                for (long v : datos.get(est).get(op).values())
                    if (v > maxTiempo)
                        maxTiempo = v;
            }
        }

        int areaAncho = ANCHO - MARGEN_I - MARGEN_D;
        int areaAlto = ALTO - MARGEN_S - MARGEN_B;

        dibujarEjes(g, areaAncho, areaAlto, tamanios, maxTiempo, "Tiempo (ns)");
        dibujarTitulo(g, titulo);

        // Estilos de línea para diferenciar push/enqueue vs pop/dequeue
        float[] dashPush = null;
        float[] dashPop = { 8f, 4f };

        for (String[] par : pares) {
            String est = par[0], op = par[1];
            if (!datos.containsKey(est) || !datos.get(est).containsKey(op))
                continue;
            Map<Integer, Long> puntos = datos.get(est).get(op);
            Color color = COLORES.getOrDefault(est, Color.GRAY);
            g.setColor(color);
            boolean esPush = op.equals("push") || op.equals("enqueue");
            g.setStroke(esPush
                    ? new BasicStroke(2.5f)
                    : new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[] { 8f, 4f },
                            0f));

            int[] xs = new int[tamanios.length];
            int[] ys = new int[tamanios.length];
            for (int i = 0; i < tamanios.length; i++) {
                xs[i] = MARGEN_I + (int) ((double) i / (tamanios.length - 1) * areaAncho);
                long val = puntos.getOrDefault(tamanios[i], 0L);
                ys[i] = MARGEN_S + areaAlto - (int) ((double) val / maxTiempo * areaAlto);
            }
            for (int i = 0; i < xs.length - 1; i++) {
                g.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                g.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
            }
            g.fillOval(xs[xs.length - 1] - 4, ys[ys.length - 1] - 4, 8, 8);
        }

        // Leyenda manual
        int lx = MARGEN_I + 10, ly = ALTO - MARGEN_B + 20;
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int col = 0;
        String[] etiquetas = { "StackArray push", "StackLista push", "QueueArray enqueue", "QueueLista enqueue",
                "StackArray pop", "StackLista pop", "QueueArray dequeue", "QueueLista dequeue" };
        String[] estNombres = { "StackArray", "StackLista", "QueueArray", "QueueLista",
                "StackArray", "StackLista", "QueueArray", "QueueLista" };
        for (int i = 0; i < etiquetas.length; i++) {
            int ex = lx + (col % 4) * 200;
            int ey = ly + (col / 4) * 18;
            g.setColor(COLORES.getOrDefault(estNombres[i], Color.GRAY));
            g.fillRect(ex, ey - 10, 16, 12);
            g.setColor(Color.DARK_GRAY);
            g.drawString(etiquetas[i], ex + 20, ey);
            col++;
        }

        g.dispose();
        ImageIO.write(img, "PNG", new File(archivo));
        System.out.println("Gráfico guardado: " + archivo);
    }

    // ─── Helpers de dibujo ──────────────────────

    private static void dibujarEjes(Graphics2D g, int areaAncho, int areaAlto,
            int[] tamanios, long maxVal, String etiqY) {
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1.5f));
        // Eje Y
        g.drawLine(MARGEN_I, MARGEN_S, MARGEN_I, MARGEN_S + areaAlto);
        // Eje X
        g.drawLine(MARGEN_I, MARGEN_S + areaAlto, MARGEN_I + areaAncho, MARGEN_S + areaAlto);

        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        // Etiquetas X
        for (int i = 0; i < tamanios.length; i++) {
            int x = MARGEN_I + (int) ((double) i / (tamanios.length - 1) * areaAncho);
            g.drawString(String.valueOf(tamanios[i]), x - 15, MARGEN_S + areaAlto + 18);
        }
        // Etiquetas Y (5 niveles)
        for (int j = 0; j <= 5; j++) {
            int y = MARGEN_S + areaAlto - (int) ((double) j / 5 * areaAlto);
            long val = (long) ((double) j / 5 * maxVal);
            g.drawString(formatNum(val), 5, y + 4);
            g.setColor(new Color(220, 220, 220));
            g.drawLine(MARGEN_I, y, MARGEN_I + areaAncho, y);
            g.setColor(Color.DARK_GRAY);
        }
        // Etiqueta eje Y
        Graphics2D g2 = (Graphics2D) g.create();
        g2.rotate(-Math.PI / 2, 15, MARGEN_S + areaAlto / 2);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2.drawString(etiqY, 15, MARGEN_S + areaAlto / 2);
        g2.dispose();
        // Etiqueta eje X
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        g.drawString("Número de elementos (N)", MARGEN_I + areaAncho / 2 - 70, MARGEN_S + areaAlto + 35);
    }

    private static void dibujarTitulo(Graphics2D g, String titulo) {
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(new Color(40, 40, 40));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, 25);
    }

    private static void dibujarLeyenda(Graphics2D g, List<String> estructuras,
            Map<String, Map<String, Map<Integer, Long>>> datos,
            String operacion) {
        int x = MARGEN_I + 10;
        int y = ALTO - MARGEN_B + 30;
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int col = 0;
        for (String est : estructuras) {
            if (!datos.containsKey(est) || !datos.get(est).containsKey(operacion))
                continue;
            int lx = x + col * 190;
            int ly = y + (col / 4) * 18;
            g.setColor(COLORES.getOrDefault(est, Color.GRAY));
            g.fillRect(lx, ly - 10, 16, 12);
            g.setColor(Color.DARK_GRAY);
            g.drawString(est, lx + 20, ly);
            col++;
        }
    }

    private static void dibujarLeyendaMemoria(Graphics2D g, List<String> estructuras,
            Map<String, Map<Integer, Long>> datos) {
        int x = MARGEN_I + 10;
        int y = ALTO - MARGEN_B + 30;
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int col = 0;
        for (String est : estructuras) {
            if (!datos.containsKey(est))
                continue;
            int lx = x + col * 190;
            int ly = y + (col / 4) * 18;
            g.setColor(COLORES.getOrDefault(est, Color.GRAY));
            g.fillRect(lx, ly - 10, 16, 12);
            g.setColor(Color.DARK_GRAY);
            g.drawString(est, lx + 20, ly);
            col++;
        }
    }

    private static String formatNum(long v) {
        if (v >= 1_000_000)
            return (v / 1_000_000) + "M";
        if (v >= 1_000)
            return (v / 1_000) + "K";
        return String.valueOf(v);
    }
}