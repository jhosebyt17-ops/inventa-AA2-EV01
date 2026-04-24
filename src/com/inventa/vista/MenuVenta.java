package com.inventa.vista;

import com.inventa.dao.ProductoDAO;
import com.inventa.dao.VentaDAO;
import com.inventa.modelo.DetalleVenta;
import com.inventa.modelo.Producto;
import com.inventa.modelo.Venta;
import com.inventa.util.Validador;

import java.util.List;

/**
 * Clase MenuVenta
 * Gestiona la interfaz de consola para las operaciones CRUD de ventas.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class MenuVenta {

    private final VentaDAO    ventaDAO;
    private final ProductoDAO productoDAO;

    /** Constructor */
    public MenuVenta() {
        this.ventaDAO    = new VentaDAO();
        this.productoDAO = new ProductoDAO();
    }

    // ─── Menú principal ──────────────────────────────────────────────

    /**
     * Muestra el menú de gestión de ventas y procesa la opción elegida.
     */
    public void mostrarMenu() {
        int opcion;
        do {
            Validador.mostrarEncabezado("INVENTA - Gestión de Ventas");
            System.out.println("  1. Registrar nueva venta");
            System.out.println("  2. Consultar todas las ventas");
            System.out.println("  3. Ver detalle de una venta");
            System.out.println("  4. Anular / cambiar estado de venta");
            System.out.println("  5. Eliminar venta");
            System.out.println("  0. Volver al menú principal");
            Validador.mostrarSeparador();
            opcion = Validador.leerEntero("  Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarVenta();
                case 2 -> consultarTodas();
                case 3 -> verDetalle();
                case 4 -> cambiarEstado();
                case 5 -> eliminarVenta();
                case 0 -> System.out.println("\n  Volviendo al menú principal...");
                default -> System.out.println("\n  [!] Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // ─── Registrar venta ─────────────────────────────────────────────

    private void registrarVenta() {
        Validador.mostrarEncabezado("Registrar Nueva Venta");

        int idCliente = Validador.leerEntero("  ID del cliente: ");
        Venta venta   = new Venta();
        venta.setIdCliente(idCliente);

        double totalVenta = 0;
        String continuar;

        do {
            System.out.println("\n  --- Agregar producto ---");

            // Mostrar productos disponibles
            List<Producto> productos = productoDAO.consultarTodos();
            productos.forEach(p -> System.out.println("  " + p));

            int idProducto = Validador.leerEntero("\n  ID del producto: ");
            Producto producto = productoDAO.consultarPorId(idProducto);

            if (producto == null) {
                System.out.println("  [!] Producto no encontrado.");
            } else if (producto.getStock() <= 0) {
                System.out.println("  [!] Producto sin stock disponible.");
            } else {
                System.out.println("  Stock disponible: " + producto.getStock());
                int cantidad = Validador.leerEntero("  Cantidad:        ");

                if (cantidad > producto.getStock()) {
                    System.out.println("  [!] Cantidad supera el stock disponible.");
                } else {
                    double subtotal = cantidad * producto.getPrecio();
                    DetalleVenta detalle = new DetalleVenta(0, idProducto, cantidad, producto.getPrecio());
                    venta.agregarDetalle(detalle);
                    totalVenta += subtotal;
                    System.out.printf("  ✓ Agregado: %s x%d = $%.2f%n",
                        producto.getNombre(), cantidad, subtotal);
                }
            }

            continuar = Validador.leerTexto("\n  ¿Agregar otro producto? (s/n): ");
        } while (continuar.equalsIgnoreCase("s"));

        if (venta.getDetalles().isEmpty()) {
            System.out.println("\n  [!] La venta no tiene productos. Operación cancelada.");
            return;
        }

        venta.setTotal(totalVenta);
        System.out.printf("\n  Total de la venta: $%.2f%n", totalVenta);

        String confirmar = Validador.leerTexto("  ¿Confirmar venta? (s/n): ");
        if (confirmar.equalsIgnoreCase("s")) {
            boolean exito = ventaDAO.insertar(venta);
            // Descontar stock de cada producto
            if (exito) {
                for (DetalleVenta d : venta.getDetalles()) {
                    productoDAO.descontarStock(d.getIdProducto(), d.getCantidad());
                }
                System.out.println("\n  ✓ Venta registrada exitosamente.");
            } else {
                System.out.println("\n  ✗ Error al registrar la venta.");
            }
        } else {
            System.out.println("\n  Operación cancelada.");
        }
    }

    // ─── Consultar todas ─────────────────────────────────────────────

    private void consultarTodas() {
        Validador.mostrarEncabezado("Lista de Ventas");
        List<Venta> lista = ventaDAO.consultarTodas();
        if (lista.isEmpty()) {
            System.out.println("  No hay ventas registradas.");
        } else {
            lista.forEach(v -> System.out.println("  " + v));
        }
        System.out.println();
    }

    // ─── Ver detalle ─────────────────────────────────────────────────

    private void verDetalle() {
        Validador.mostrarEncabezado("Detalle de Venta");
        int id = Validador.leerEntero("  ID de la venta: ");
        Venta v = ventaDAO.consultarPorId(id);
        if (v == null) {
            System.out.println("\n  [!] Venta no encontrada.");
            return;
        }
        System.out.println("\n  " + v);
        System.out.println("  Productos:");
        v.getDetalles().forEach(d -> System.out.println(d));
        System.out.printf("  TOTAL: $%.2f%n%n", v.getTotal());
    }

    // ─── Cambiar estado ──────────────────────────────────────────────

    private void cambiarEstado() {
        Validador.mostrarEncabezado("Cambiar Estado de Venta");
        int id = Validador.leerEntero("  ID de la venta: ");
        System.out.println("  Estados disponibles: ACTIVA / ANULADA / PAGADA");
        String nuevoEstado = Validador.leerTexto("  Nuevo estado: ").toUpperCase();
        boolean exito = ventaDAO.actualizarEstado(id, nuevoEstado);
        System.out.println(exito
            ? "\n  ✓ Estado actualizado exitosamente."
            : "\n  ✗ Error al actualizar el estado.");
    }

    // ─── Eliminar venta ──────────────────────────────────────────────

    private void eliminarVenta() {
        Validador.mostrarEncabezado("Eliminar Venta");
        int id = Validador.leerEntero("  ID de la venta a eliminar: ");
        Venta v = ventaDAO.consultarPorId(id);
        if (v == null) {
            System.out.println("\n  [!] Venta no encontrada.");
            return;
        }
        System.out.println("  Venta a eliminar: " + v);
        String confirmacion = Validador.leerTexto("  ¿Confirma la eliminación? (s/n): ");
        if (confirmacion.equalsIgnoreCase("s")) {
            boolean exito = ventaDAO.eliminar(id);
            System.out.println(exito
                ? "\n  ✓ Venta eliminada exitosamente."
                : "\n  ✗ Error al eliminar la venta.");
        } else {
            System.out.println("\n  Operación cancelada.");
        }
    }
}
