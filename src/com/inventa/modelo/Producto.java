package com.inventa.modelo;

/**
 * Clase Producto
 * Representa la entidad Producto del sistema de ventas Inventa.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class Producto {

    private int    idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private int    stock;
    private String categoria;

    // ─── Constructores ───────────────────────────────────────────────

    /** Constructor vacío */
    public Producto() {}

    /** Constructor completo */
    public Producto(int idProducto, String nombre, String descripcion,
                    double precio, int stock, String categoria) {
        this.idProducto  = idProducto;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.stock       = stock;
        this.categoria   = categoria;
    }

    /** Constructor sin ID (para inserciones nuevas) */
    public Producto(String nombre, String descripcion,
                    double precio, int stock, String categoria) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.stock       = stock;
        this.categoria   = categoria;
    }

    // ─── Getters y Setters ───────────────────────────────────────────

    public int getIdProducto()                  { return idProducto; }
    public void setIdProducto(int idProducto)   { this.idProducto = idProducto; }

    public String getNombre()                   { return nombre; }
    public void setNombre(String nombre)        { this.nombre = nombre; }

    public String getDescripcion()                   { return descripcion; }
    public void setDescripcion(String descripcion)   { this.descripcion = descripcion; }

    public double getPrecio()               { return precio; }
    public void setPrecio(double precio)    { this.precio = precio; }

    public int getStock()            { return stock; }
    public void setStock(int stock)  { this.stock = stock; }

    public String getCategoria()                { return categoria; }
    public void setCategoria(String categoria)  { this.categoria = categoria; }

    // ─── toString ────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "Producto [ID: %d | Nombre: %-20s | Precio: $%10.2f | Stock: %3d | Categoría: %s]",
            idProducto, nombre, precio, stock, categoria
        );
    }
}
