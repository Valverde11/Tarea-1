package benchmark;

import estructuras.*;

/**
 * Escenario 1: Historial de acciones (Undo) - Stack es correcto, Queue es incorrecto.
 * Escenario 2: Sistema de atención FIFO     - Queue es correcto, Stack es incorrecto.
 */
public class Escenarios {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ESCENARIO 1: HISTORIAL DE ACCIONES (UNDO)");
        System.out.println("========================================");
        escenarioUndoStack();
        escenarioUndoQueue();

        System.out.println("\n========================================");
        System.out.println("  ESCENARIO 2: SISTEMA DE ATENCIÓN (FIFO)");
        System.out.println("========================================");
        escenarioAtencionQueue();
        escenarioAtencionStack();
    }

    // ─────────────────────────────────────────────
    // ESCENARIO 1 - STACK (CORRECTO para UNDO)
    // ─────────────────────────────────────────────
    private static void escenarioUndoStack() {
        System.out.println("\n[Stack con Array - CORRECTO para Undo]");
        StackArray<String> historial = new StackArray<>(10);

        // El usuario realiza acciones
        String[] acciones = {"Escribir 'Hola'", "Negrita", "Cambiar fuente", "Insertar imagen", "Guardar"};
        for (String accion : acciones) {
            historial.push(accion);
            System.out.println("  Acción realizada: " + accion);
        }

        // El usuario deshace acciones (LIFO: la última acción es la primera en deshacerse)
        System.out.println("\n  -- Deshacer acciones --");
        int deshacer = 3;
        for (int i = 0; i < deshacer && !historial.estaVacio(); i++) {
            System.out.println("  Deshecho: " + historial.pop());
        }

        System.out.println("\n[Stack con Lista - CORRECTO para Undo]");
        StackLista<String> historialLista = new StackLista<>();
        for (String accion : acciones) historialLista.push(accion);
        System.out.println("  -- Deshacer acciones --");
        for (int i = 0; i < deshacer && !historialLista.estaVacio(); i++) {
            System.out.println("  Deshecho: " + historialLista.pop());
        }
    }

    private static void escenarioUndoQueue() {
        System.out.println("\n[Queue con Array - INCORRECTO para Undo]");
        QueueArray<String> historial = new QueueArray<>(10);

        String[] acciones = {"Escribir 'Hola'", "Negrita", "Cambiar fuente", "Insertar imagen", "Guardar"};
        for (String accion : acciones) historial.enqueue(accion);

        System.out.println("  -- Intentar deshacer con Queue (saca la MÁS ANTIGUA, no la última) --");
        int deshacer = 3;
        for (int i = 0; i < deshacer && !historial.estaVacia(); i++) {
            System.out.println("  'Deshecho' (incorrecto): " + historial.dequeue() + "  <-- Debería ser la última acción!");
        }

        System.out.println("\n[Queue con Lista - INCORRECTO para Undo]");
        QueueLista<String> historialLista = new QueueLista<>();
        for (String accion : acciones) historialLista.enqueue(accion);
        for (int i = 0; i < deshacer && !historialLista.estaVacia(); i++) {
            System.out.println("  'Deshecho' (incorrecto): " + historialLista.dequeue() + "  <-- Orden equivocado!");
        }
    }

    // ─────────────────────────────────────────────
    // ESCENARIO 2 - QUEUE (CORRECTO para FIFO)
    // ─────────────────────────────────────────────
    private static void escenarioAtencionQueue() {
        System.out.println("\n[Queue con Array - CORRECTO para atención FIFO]");
        QueueArray<String> fila = new QueueArray<>(10);

        String[] clientes = {"Cliente A", "Cliente B", "Cliente C", "Cliente D", "Cliente E"};
        for (String cliente : clientes) {
            fila.enqueue(cliente);
            System.out.println("  Llegó: " + cliente);
        }

        System.out.println("\n  -- Atendiendo clientes --");
        int atender = 3;
        for (int i = 0; i < atender && !fila.estaVacia(); i++) {
            System.out.println("  Atendido: " + fila.dequeue() + "  (el primero en llegar)");
        }

        System.out.println("\n[Queue con Lista - CORRECTO para atención FIFO]");
        QueueLista<String> filaLista = new QueueLista<>();
        for (String cliente : clientes) filaLista.enqueue(cliente);
        for (int i = 0; i < atender && !filaLista.estaVacia(); i++) {
            System.out.println("  Atendido: " + filaLista.dequeue());
        }
    }

    private static void escenarioAtencionStack() {
        System.out.println("\n[Stack con Array - INCORRECTO para atención FIFO]");
        StackArray<String> fila = new StackArray<>(10);

        String[] clientes = {"Cliente A", "Cliente B", "Cliente C", "Cliente D", "Cliente E"};
        for (String cliente : clientes) fila.push(cliente);

        System.out.println("  -- Atendiendo con Stack (atiende al ÚLTIMO en llegar) --");
        int atender = 3;
        for (int i = 0; i < atender && !fila.estaVacio(); i++) {
            System.out.println("  'Atendido' (incorrecto): " + fila.pop() + "  <-- Debería ser el primero en llegar!");
        }

        System.out.println("\n[Stack con Lista - INCORRECTO para atención FIFO]");
        StackLista<String> filaLista = new StackLista<>();
        for (String cliente : clientes) filaLista.push(cliente);
        for (int i = 0; i < atender && !filaLista.estaVacio(); i++) {
            System.out.println("  'Atendido' (incorrecto): " + filaLista.pop() + "  <-- Orden equivocado!");
        }
    }
}