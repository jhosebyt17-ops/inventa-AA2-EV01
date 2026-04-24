package com.inventa.vista;

import com.inventa.dao.ProductoDAO;
import com.inventa.modelo.Producto;
import com.inventa.util.Validador;

import java.util.List;

/**
 * Clase MenuProducto
 * Gestiona la interfaz de consola para las operaciones CRUD de productos.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class MenuProducto {

    private final ProductoDAO productoDAO;

    /** Constructor */
    public MenuProducto() {
        this.productoDAO = new ProductoDAO();
    }

    // ─── Menú principal ──────────────────────────────────────────────

    /**
     * Muestra el menú de gestión de productos y procesa la opción elegida.
     */
    public void mostrarMenu() {
        int opcion;
        do {
            Validador.mostrarEncabezado("INVENTA - Gestión de Productos");
            System.out.println("  1. Registrar nuevo producto");
            System.out.println("  2. Consultar todos los productos");
            System.out.println("  3. Buscar producto por ID");
            System.out.println("  4. Actualizar producto");
            System.out.println("  5. Eliminar producto");
            System.out.println("  0. Volver al menú principal");
            Validador.mostrarSeparador();
            opcion = Validador.leerEntero("  Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> consultarTodos();
                case 3 -> buscarPorId();
                case 4 -> actualizarProducto();
                case 5 -> eliminarProducto();
                case 0 -> System.out.println("\n  Volviendo al menú principal...");
                default -> System.out.println("\n  [!] Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // ─── Registrar ───────────────────────────────────────────────────

    private void registrarProducto() {
        Validador.mostrarEncabezado("Registrar Nuevo Producto");
        String nombre      = Validador.leerTexto("  Nombre:      ");
        String descripcion = Validador.leerTextoOpcional("  Descripción: ");
        double precio      = Validador.leerDouble("  Precio:      $");
        int    stock       = Validador.leerEntero("  Stock:       ");
        String categoria   = Validador.leerTextoOpcional("  Categoría:   ");

        Producto producto = new Producto(nombre, descripcion, precio, stock, categoria);
        boolean exito     = productoDAO.insertar(producto);

        System.out.println(exito
            ? "\n  ✓ Producto registrado exitosamente."
            : "\n  ✗ Error al registrar el producto.");
    }

    // ─── Consultar todos ─────────────────────────────────────────────

    private void consultarTodos() {
        Validador.mostrarEncabezado("Lista de Productos");
        List<Producto> lista = productoDAO.consultarTodos();
        if (lista.isEmpty()) {
            System.out.println("  No hay productos registrados.");
        } else {
            lista.forEach(p -> System.out.println("  " + p));
        }
        System.out.println();
    }

    // ─── Buscar por ID ───────────────────────────────────────────────

    private void buscarPorId() {
        Validador.mostrarEncabezado("Buscar Producto por ID");
        int id = Validador.leerEntero("  ID del producto: ");
        Producto p = productoDAO.consultarPorId(id);
        if (p != null) {
            System.out.println("\n  " + p);
        } else {
            System.out.println("\n  [!] Producto con ID " + id + " no encontrado.");
        }
    }

    // ─── Actualizar ──────────────────────────────────────────────────

    private void actualizarProducto() {
        Validador.mostrarEncabezado("Actualizar Producto");
        int id = Validador.leerEntero("  ID del producto a actualizar: ");
        Producto existente = productoDAO.consultarPorId(id);
        if (existente == null) {
            System.out.println("\n  [!] Producto no encontrado.");
            return;
        }
        System.out.println("  Producto actual: " + existente);
        System.out.println("  (Deje el campo vacío para conservar el valor actual)");

        String nombre      = Validador.leerTextoOpcional("  Nuevo nombre      [" + existente.getNombre()      + "]: ");
        String descripcion = Validador.leerTextoOpcional("  Nueva descripción [" + existente.getDescripcion() + "]: ");
        String precioStr   = Validador.leerTextoOpcional("  Nuevo precio      [" + existente.getPrecio()      + "]: ");
        String stockStr    = Validador.leerTextoOpcional("  Nuevo stock       [" + existente.getStock()       + "]: ");
        String categoria   = Validador.leerTextoOpcional("  Nueva categoría   [" + existente.getCategoria()   + "]: ");

        if (!nombre.isEmpty())      existente.setNombre(nombre);
        if (!descripcion.isEmpty()) existente.setDescripcion(descripcion);
        if (!precioStr.isEmpty()) {
            try { existente.setPrecio(Double.parseDouble(precioStr.replace(",","."))); }
            catch (NumberFormatException e) { System.out.println("  [!] Precio inválido, se conserva el actual."); }
        }
        if (!stockStr.isEmpty()) {
            try { existente.setStock(Integer.parseInt(stockStr)); }
            catch (NumberFormatException e) { System.out.println("  [!] Stock inválido, se conserva el actual."); }
        }
        if (!categoria.isEmpty()) existente.setCategoria(categoria);

        boolean exito = productoDAO.actualizar(existente);
        System.out.println(exito
            ? "\n  ✓ Producto actualizado exitosamente."
            : "\n  ✗ Error al actualizar el producto.");
    }

    // ─── Eliminar ────────────────────────────────────────────────────

    private void eliminarProducto() {
        Validador.mostrarEncabezado("Eliminar Producto");
        int id = Validador.leerEntero("  ID del producto a eliminar: ");
        Producto p = productoDAO.consultarPorId(id);
        if (p == null) {
            System.out.println("\n  [!] Producto no encontrado.");
            return;
        }
        System.out.println("  Producto a eliminar: " + p);
        String confirmacion = Validador.leerTexto("  ¿Confirma la eliminación? (s/n): ");
        if (confirmacion.equalsIgnoreCase("s")) {
            boolean exito = productoDAO.eliminar(id);
            System.out.println(exito
                ? "\n  ✓ Producto eliminado exitosamente."
                : "\n  ✗ Error al eliminar el producto.");
        } else {
            System.out.println("\n  Operación cancelada.");
        }
    }
}
