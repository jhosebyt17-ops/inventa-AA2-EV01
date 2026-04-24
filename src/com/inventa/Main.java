package com.inventa;

import com.inventa.conexion.ConexionDB;
import com.inventa.util.Validador;
import com.inventa.vista.MenuCliente;
import com.inventa.vista.MenuProducto;
import com.inventa.vista.MenuVenta;

/**
 * Clase Main
 * Punto de entrada principal del sistema de ventas INVENTA.
 * Muestra el menú principal y redirige a cada módulo del sistema.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class Main {

    /**
     * Método principal de la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        // Verificar conexión al inicio
        ConexionDB.obtenerInstancia().obtenerConexion();

        MenuCliente  menuCliente  = new MenuCliente();
        MenuProducto menuProducto = new MenuProducto();
        MenuVenta    menuVenta    = new MenuVenta();

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = Validador.leerEntero("  Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuCliente.mostrarMenu();
                case 2 -> menuProducto.mostrarMenu();
                case 3 -> menuVenta.mostrarMenu();
                case 0 -> {
                    System.out.println("\n  ¡Gracias por usar INVENTA! Hasta pronto.\n");
                    ConexionDB.obtenerInstancia().cerrarConexion();
                }
                default -> System.out.println("\n  [!] Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    /**
     * Imprime el menú principal en consola.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║       INVENTA - Sistema de Ventas    ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  1. Gestión de Clientes              ║");
        System.out.println("  ║  2. Gestión de Productos             ║");
        System.out.println("  ║  3. Gestión de Ventas                ║");
        System.out.println("  ║  0. Salir                            ║");
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}
