package com.inventa.util;

import java.util.Scanner;

/**
 * Clase Validador
 * Proporciona métodos utilitarios para validar entradas del usuario
 * y leer datos desde consola de forma segura.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class Validador {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lee un entero positivo desde consola.
     * Repite la solicitud si el usuario ingresa un valor no válido.
     *
     * @param mensaje texto a mostrar al usuario
     * @return entero válido ingresado por el usuario
     */
    public static int leerEntero(String mensaje) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            try {
                valor = Integer.parseInt(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Ingrese un número entero válido.");
            }
        }
        return valor;
    }

    /**
     * Lee un número decimal (double) desde consola.
     *
     * @param mensaje texto a mostrar al usuario
     * @return double válido ingresado por el usuario
     */
    public static double leerDouble(String mensaje) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            try {
                valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (valor < 0) {
                    System.out.println("  [!] El valor no puede ser negativo.");
                } else {
                    valido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Ingrese un número decimal válido (ej: 1500.50).");
            }
        }
        return valor;
    }

    /**
     * Lee una cadena de texto no vacía desde consola.
     *
     * @param mensaje texto a mostrar al usuario
     * @return String no vacío ingresado por el usuario
     */
    public static String leerTexto(String mensaje) {
        String valor = "";
        while (valor.isEmpty()) {
            System.out.print(mensaje);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("  [!] El campo no puede estar vacío.");
            }
        }
        return valor;
    }

    /**
     * Lee una cadena de texto que puede ser vacía (campo opcional).
     *
     * @param mensaje texto a mostrar al usuario
     * @return String ingresado por el usuario (puede ser vacío)
     */
    public static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    /**
     * Muestra una línea separadora en consola.
     */
    public static void mostrarSeparador() {
        System.out.println("─".repeat(60));
    }

    /**
     * Muestra un encabezado con título centrado.
     *
     * @param titulo texto del encabezado
     */
    public static void mostrarEncabezado(String titulo) {
        System.out.println();
        mostrarSeparador();
        System.out.printf("  %s%n", titulo.toUpperCase());
        mostrarSeparador();
    }
}
