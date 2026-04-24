package com.inventa.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionDB
 * Gestiona la conexión a la base de datos MySQL mediante JDBC.
 * Implementa el patrón Singleton para reutilizar la conexión.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class ConexionDB {

    // Parámetros de conexión
    private static final String URL      = "jdbc:mysql://localhost:3306/inventa_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO  = "root";
    private static final String CLAVE    = "";       // Cambiar según configuración local
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    // Instancia única (Singleton)
    private static ConexionDB instancia;
    private Connection conexion;

    /**
     * Constructor privado: carga el driver y establece la conexión.
     */
    private ConexionDB() {
        try {
            Class.forName(DRIVER);
            this.conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("[INFO] Conexión a la base de datos establecida.");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] Driver JDBC no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al conectar con la BD: " + e.getMessage());
        }
    }

    /**
     * Retorna la instancia única de ConexionDB (Singleton).
     *
     * @return instancia de ConexionDB
     */
    public static ConexionDB obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Retorna el objeto Connection activo.
     *
     * @return objeto Connection de JDBC
     */
    public Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                instancia = new ConexionDB();
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Verificando conexión: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa con la base de datos.
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[INFO] Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Al cerrar la conexión: " + e.getMessage());
        }
    }
}
