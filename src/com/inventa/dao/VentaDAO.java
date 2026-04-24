package com.inventa.dao;

import com.inventa.conexion.ConexionDB;
import com.inventa.modelo.DetalleVenta;
import com.inventa.modelo.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase VentaDAO
 * Gestiona las operaciones CRUD de las entidades Venta y DetalleVenta
 * contra la base de datos MySQL mediante JDBC.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class VentaDAO {

    private Connection conexion;

    /** Constructor: obtiene la conexión activa */
    public VentaDAO() {
        this.conexion = ConexionDB.obtenerInstancia().obtenerConexion();
    }

    // ═══════════════════════════════════════════════════════
    //  INSERTAR VENTA (cabecera + detalles)
    // ═══════════════════════════════════════════════════════

    /**
     * Registra una nueva venta con sus detalles en la base de datos.
     * Utiliza transacción para garantizar la integridad de los datos.
     *
     * @param venta objeto Venta con su lista de detalles
     * @return true si la transacción fue exitosa
     */
    public boolean insertar(Venta venta) {
        String sqlVenta   = "INSERT INTO ventas (id_cliente, total, estado) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) "
                          + "VALUES (?, ?, ?, ?, ?)";
        try {
            // Desactivar auto-commit para manejar transacción manualmente
            conexion.setAutoCommit(false);

            int idVentaGenerado;

            // Insertar cabecera de la venta
            try (PreparedStatement ps = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1,    venta.getIdCliente());
                ps.setDouble(2, venta.getTotal());
                ps.setString(3, venta.getEstado());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idVentaGenerado = rs.getInt(1);
                    } else {
                        conexion.rollback();
                        return false;
                    }
                }
            }

            // Insertar cada línea de detalle
            try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    ps.setInt(1,    idVentaGenerado);
                    ps.setInt(2,    detalle.getIdProducto());
                    ps.setInt(3,    detalle.getCantidad());
                    ps.setDouble(4, detalle.getPrecioUnitario());
                    ps.setDouble(5, detalle.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conexion.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("[ERROR] Al insertar venta (transacción revertida): " + e.getMessage());
            try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { conexion.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ═══════════════════════════════════════════════════════
    //  CONSULTAR TODAS LAS VENTAS
    // ═══════════════════════════════════════════════════════

    /**
     * Obtiene la lista de todas las ventas con el nombre del cliente.
     *
     * @return List con todas las ventas
     */
    public List<Venta> consultarTodas() {
        List<Venta> listaVentas = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.id_cliente, "
                   + "CONCAT(c.nombre,' ',c.apellido) AS nombre_cliente, "
                   + "v.fecha_venta, v.total, v.estado "
                   + "FROM ventas v "
                   + "JOIN clientes c ON v.id_cliente = c.id_cliente "
                   + "ORDER BY v.id_venta DESC";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id_venta"),
                    rs.getInt("id_cliente"),
                    rs.getString("fecha_venta"),
                    rs.getDouble("total"),
                    rs.getString("estado")
                );
                v.setNombreCliente(rs.getString("nombre_cliente"));
                listaVentas.add(v);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Al consultar ventas: " + e.getMessage());
        }
        return listaVentas;
    }

    // ═══════════════════════════════════════════════════════
    //  CONSULTAR VENTA POR ID (con detalles)
    // ═══════════════════════════════════════════════════════

    /**
     * Busca una venta por su ID e incluye sus detalles.
     *
     * @param idVenta identificador de la venta
     * @return objeto Venta completo con detalles, o null si no existe
     */
    public Venta consultarPorId(int idVenta) {
        String sqlVenta   = "SELECT v.id_venta, v.id_cliente, "
                          + "CONCAT(c.nombre,' ',c.apellido) AS nombre_cliente, "
                          + "v.fecha_venta, v.total, v.estado "
                          + "FROM ventas v JOIN clientes c ON v.id_cliente = c.id_cliente "
                          + "WHERE v.id_venta = ?";
        String sqlDetalle = "SELECT dv.id_detalle, dv.id_venta, dv.id_producto, "
                          + "p.nombre AS nombre_producto, dv.cantidad, "
                          + "dv.precio_unitario, dv.subtotal "
                          + "FROM detalle_venta dv "
                          + "JOIN productos p ON dv.id_producto = p.id_producto "
                          + "WHERE dv.id_venta = ?";
        try {
            Venta venta = null;

            // Cargar cabecera
            try (PreparedStatement ps = conexion.prepareStatement(sqlVenta)) {
                ps.setInt(1, idVenta);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        venta = new Venta(
                            rs.getInt("id_venta"),
                            rs.getInt("id_cliente"),
                            rs.getString("fecha_venta"),
                            rs.getDouble("total"),
                            rs.getString("estado")
                        );
                        venta.setNombreCliente(rs.getString("nombre_cliente"));
                    }
                }
            }

            if (venta == null) return null;

            // Cargar detalles
            try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {
                ps.setInt(1, idVenta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        DetalleVenta d = new DetalleVenta(
                            rs.getInt("id_detalle"),
                            rs.getInt("id_venta"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario"),
                            rs.getDouble("subtotal")
                        );
                        d.setNombreProducto(rs.getString("nombre_producto"));
                        venta.agregarDetalle(d);
                    }
                }
            }

            return venta;

        } catch (SQLException e) {
            System.err.println("[ERROR] Al buscar venta por ID: " + e.getMessage());
            return null;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  ACTUALIZAR ESTADO DE VENTA
    // ═══════════════════════════════════════════════════════

    /**
     * Actualiza el estado de una venta (ACTIVA / ANULADA).
     *
     * @param idVenta identificador de la venta
     * @param estado  nuevo estado
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarEstado(int idVenta, String estado) {
        String sql = "UPDATE ventas SET estado = ? WHERE id_venta = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idVenta);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al actualizar estado de venta: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  ELIMINAR VENTA
    // ═══════════════════════════════════════════════════════

    /**
     * Elimina una venta y sus detalles de la base de datos.
     * Utiliza transacción para garantizar integridad.
     *
     * @param idVenta identificador de la venta a eliminar
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int idVenta) {
        String sqlDetalle = "DELETE FROM detalle_venta WHERE id_venta = ?";
        String sqlVenta   = "DELETE FROM ventas WHERE id_venta = ?";
        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {
                ps.setInt(1, idVenta);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conexion.prepareStatement(sqlVenta)) {
                ps.setInt(1, idVenta);
                ps.executeUpdate();
            }

            conexion.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("[ERROR] Al eliminar venta (transacción revertida): " + e.getMessage());
            try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { conexion.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
