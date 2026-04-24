package com.inventa.vista;

import com.inventa.dao.ClienteDAO;
import com.inventa.modelo.Cliente;
import com.inventa.util.Validador;

import java.util.List;

/**
 * Clase MenuCliente
 * Gestiona la interfaz de consola para las operaciones CRUD de clientes.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class MenuCliente {

    private final ClienteDAO clienteDAO;

    /** Constructor */
    public MenuCliente() {
        this.clienteDAO = new ClienteDAO();
    }

    // ─── Menú principal ──────────────────────────────────────────────

    /**
     * Muestra el menú de gestión de clientes y procesa la opción elegida.
     */
    public void mostrarMenu() {
        int opcion;
        do {
            Validador.mostrarEncabezado("INVENTA - Gestión de Clientes");
            System.out.println("  1. Registrar nuevo cliente");
            System.out.println("  2. Consultar todos los clientes");
            System.out.println("  3. Buscar cliente por ID");
            System.out.println("  4. Actualizar cliente");
            System.out.println("  5. Eliminar cliente");
            System.out.println("  0. Volver al menú principal");
            Validador.mostrarSeparador();
            opcion = Validador.leerEntero("  Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarCliente();
                case 2 -> consultarTodos();
                case 3 -> buscarPorId();
                case 4 -> actualizarCliente();
                case 5 -> eliminarCliente();
                case 0 -> System.out.println("\n  Volviendo al menú principal...");
                default -> System.out.println("\n  [!] Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // ─── Registrar ───────────────────────────────────────────────────

    private void registrarCliente() {
        Validador.mostrarEncabezado("Registrar Nuevo Cliente");
        String nombre    = Validador.leerTexto("  Nombre:     ");
        String apellido  = Validador.leerTexto("  Apellido:   ");
        String documento = Validador.leerTexto("  Documento:  ");
        String correo    = Validador.leerTextoOpcional("  Correo:     ");
        String telefono  = Validador.leerTextoOpcional("  Teléfono:   ");
        String direccion = Validador.leerTextoOpcional("  Dirección:  ");

        Cliente cliente = new Cliente(nombre, apellido, documento, correo, telefono, direccion);
        boolean exito   = clienteDAO.insertar(cliente);

        System.out.println(exito
            ? "\n  ✓ Cliente registrado exitosamente."
            : "\n  ✗ Error al registrar el cliente.");
    }

    // ─── Consultar todos ─────────────────────────────────────────────

    private void consultarTodos() {
        Validador.mostrarEncabezado("Lista de Clientes");
        List<Cliente> lista = clienteDAO.consultarTodos();
        if (lista.isEmpty()) {
            System.out.println("  No hay clientes registrados.");
        } else {
            lista.forEach(c -> System.out.println("  " + c));
        }
        System.out.println();
    }

    // ─── Buscar por ID ───────────────────────────────────────────────

    private void buscarPorId() {
        Validador.mostrarEncabezado("Buscar Cliente por ID");
        int id = Validador.leerEntero("  ID del cliente: ");
        Cliente c = clienteDAO.consultarPorId(id);
        if (c != null) {
            System.out.println("\n  " + c);
        } else {
            System.out.println("\n  [!] Cliente con ID " + id + " no encontrado.");
        }
    }

    // ─── Actualizar ──────────────────────────────────────────────────

    private void actualizarCliente() {
        Validador.mostrarEncabezado("Actualizar Cliente");
        int id = Validador.leerEntero("  ID del cliente a actualizar: ");
        Cliente existente = clienteDAO.consultarPorId(id);
        if (existente == null) {
            System.out.println("\n  [!] Cliente no encontrado.");
            return;
        }
        System.out.println("  Cliente actual: " + existente);
        System.out.println("  (Deje el campo vacío para conservar el valor actual)");

        String nombre    = Validador.leerTextoOpcional("  Nuevo nombre    [" + existente.getNombre()    + "]: ");
        String apellido  = Validador.leerTextoOpcional("  Nuevo apellido  [" + existente.getApellido()  + "]: ");
        String documento = Validador.leerTextoOpcional("  Nuevo documento [" + existente.getDocumento() + "]: ");
        String correo    = Validador.leerTextoOpcional("  Nuevo correo    [" + existente.getCorreo()    + "]: ");
        String telefono  = Validador.leerTextoOpcional("  Nuevo teléfono  [" + existente.getTelefono()  + "]: ");
        String direccion = Validador.leerTextoOpcional("  Nueva dirección [" + existente.getDireccion() + "]: ");

        // Conservar valores actuales si el campo queda vacío
        existente.setNombre   (nombre.isEmpty()    ? existente.getNombre()    : nombre);
        existente.setApellido (apellido.isEmpty()  ? existente.getApellido()  : apellido);
        existente.setDocumento(documento.isEmpty() ? existente.getDocumento() : documento);
        existente.setCorreo   (correo.isEmpty()    ? existente.getCorreo()    : correo);
        existente.setTelefono (telefono.isEmpty()  ? existente.getTelefono()  : telefono);
        existente.setDireccion(direccion.isEmpty() ? existente.getDireccion() : direccion);

        boolean exito = clienteDAO.actualizar(existente);
        System.out.println(exito
            ? "\n  ✓ Cliente actualizado exitosamente."
            : "\n  ✗ Error al actualizar el cliente.");
    }

    // ─── Eliminar ────────────────────────────────────────────────────

    private void eliminarCliente() {
        Validador.mostrarEncabezado("Eliminar Cliente");
        int id = Validador.leerEntero("  ID del cliente a eliminar: ");
        Cliente c = clienteDAO.consultarPorId(id);
        if (c == null) {
            System.out.println("\n  [!] Cliente no encontrado.");
            return;
        }
        System.out.println("  Cliente a eliminar: " + c);
        String confirmacion = Validador.leerTexto("  ¿Confirma la eliminación? (s/n): ");
        if (confirmacion.equalsIgnoreCase("s")) {
            boolean exito = clienteDAO.eliminar(id);
            System.out.println(exito
                ? "\n  ✓ Cliente eliminado exitosamente."
                : "\n  ✗ Error al eliminar el cliente.");
        } else {
            System.out.println("\n  Operación cancelada.");
        }
    }
}
