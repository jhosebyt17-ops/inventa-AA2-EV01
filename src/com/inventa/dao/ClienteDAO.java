package com.inventa.dao;

import com.inventa.conexion.ConexionDB;
import com.inventa.modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ClienteDAO
 * Gestiona las operaciones CRUD de la entidad Cliente
 * contra la base de datos MySQL mediante JDBC.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class ClienteDAO {

    private Connection conexion;

    /** Constructor: obtiene la conexión activa */
    public ClienteDAO() {
        this.conexion = ConexionDB.obtenerInstancia().obtenerConexion();
    }

    // ═══════════════════════════════════════════════════════
    //  INSERTAR
    // ═══════════════════════════════════════════════════════

    /**
     * Inserta un nuevo cliente en la base de datos.
     *
     * @param cliente objeto Cliente a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellido, documento, correo, telefono, direccion) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDocumento());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getDireccion());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  CONSULTAR TODOS
    // ═══════════════════════════════════════════════════════

    /**
     * Obtiene la lista completa de clientes registrados.
     *
     * @return List con todos los clientes
     */
    public List<Cliente> consultarTodos() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, apellido, documento, correo, telefono, direccion "
                   + "FROM clientes ORDER BY id_cliente";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("documento"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                );
                listaClientes.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Al consultar clientes: " + e.getMessage());
        }
        return listaClientes;
    }

    // ═══════════════════════════════════════════════════════
    //  CONSULTAR POR ID
    // ═══════════════════════════════════════════════════════

    /**
     * Busca un cliente por su ID.
     *
     * @param idCliente identificador del cliente
     * @return objeto Cliente si se encuentra, null en caso contrario
     */
    public Cliente consultarPorId(int idCliente) {
        String sql = "SELECT id_cliente, nombre, apellido, documento, correo, telefono, direccion "
                   + "FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("documento"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Al buscar cliente por ID: " + e.getMessage());
        }
        return null;
    }

    // ═══════════════════════════════════════════════════════
    //  ACTUALIZAR
    // ═══════════════════════════════════════════════════════

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param cliente objeto Cliente con los datos actualizados (debe tener ID)
     * @return true si la actualización fue exitosa
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, apellido=?, documento=?, "
                   + "correo=?, telefono=?, direccion=? WHERE id_cliente=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDocumento());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getDireccion());
            ps.setInt(7, cliente.getIdCliente());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  ELIMINAR
    // ═══════════════════════════════════════════════════════

    /**
     * Elimina un cliente de la base de datos por su ID.
     *
     * @param idCliente identificador del cliente a eliminar
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
