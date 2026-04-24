package com.inventa.dao;

import com.inventa.conexion.ConexionDB;
import com.inventa.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ProductoDAO
 * Gestiona las operaciones CRUD de la entidad Producto
 * contra la base de datos MySQL mediante JDBC.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class ProductoDAO {

    private Connection conexion;

    /** Constructor: obtiene la conexión activa */
    public ProductoDAO() {
        this.conexion = ConexionDB.obtenerInstancia().obtenerConexion();
    }

    // ═══════════════════════════════════════════════════════
    //  INSERTAR
    // ═══════════════════════════════════════════════════════

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param producto objeto Producto a insertar
     * @return true si la inserción fue exitosa
     */
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, categoria) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4,    producto.getStock());
            ps.setString(5, producto.getCategoria());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  CONSULTAR TODOS
    // ═══════════════════════════════════════════════════════

    /**
     * Obtiene la lista completa de productos registrados.
     *
     * @return List con todos los productos
     */
    public List<Producto> consultarTodos() {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, categoria "
                   + "FROM productos ORDER BY id_producto";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("categoria")
                );
                listaProductos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Al consultar productos: " + e.getMessage());
        }
        return listaProductos;
    }

    // ═══════════════════════════════════════════════════════
    //  CONSULTAR POR ID
    // ═══════════════════════════════════════════════════════

    /**
     * Busca un producto por su ID.
     *
     * @param idProducto identificador del producto
     * @return objeto Producto si se encuentra, null en caso contrario
     */
    public Producto consultarPorId(int idProducto) {
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, categoria "
                   + "FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("categoria")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Al buscar producto por ID: " + e.getMessage());
        }
        return null;
    }

    // ═══════════════════════════════════════════════════════
    //  ACTUALIZAR
    // ═══════════════════════════════════════════════════════

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param producto objeto Producto con datos actualizados (debe tener ID)
     * @return true si la actualización fue exitosa
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, "
                   + "stock=?, categoria=? WHERE id_producto=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4,    producto.getStock());
            ps.setString(5, producto.getCategoria());
            ps.setInt(6,    producto.getIdProducto());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  ACTUALIZAR STOCK
    // ═══════════════════════════════════════════════════════

    /**
     * Descuenta unidades del stock de un producto (al realizar una venta).
     *
     * @param idProducto identificador del producto
     * @param cantidad   unidades a descontar
     * @return true si la actualización fue exitosa
     */
    public boolean descontarStock(int idProducto, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al descontar stock: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  ELIMINAR
    // ═══════════════════════════════════════════════════════

    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * @param idProducto identificador del producto a eliminar
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}
