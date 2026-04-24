package com.inventa.modelo;

/**
 * Clase DetalleVenta
 * Representa el detalle (línea) de una venta en el sistema Inventa.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class DetalleVenta {

    private int    idDetalle;
    private int    idVenta;
    private int    idProducto;
    private String nombreProducto;   // Campo auxiliar para mostrar
    private int    cantidad;
    private double precioUnitario;
    private double subtotal;

    // ─── Constructores ───────────────────────────────────────────────

    /** Constructor vacío */
    public DetalleVenta() {}

    /** Constructor completo */
    public DetalleVenta(int idDetalle, int idVenta, int idProducto,
                        int cantidad, double precioUnitario, double subtotal) {
        this.idDetalle      = idDetalle;
        this.idVenta        = idVenta;
        this.idProducto     = idProducto;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal       = subtotal;
    }

    /** Constructor para nueva línea de venta */
    public DetalleVenta(int idVenta, int idProducto, int cantidad, double precioUnitario) {
        this.idVenta        = idVenta;
        this.idProducto     = idProducto;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal       = cantidad * precioUnitario;
    }

    // ─── Getters y Setters ───────────────────────────────────────────

    public int getIdDetalle()                { return idDetalle; }
    public void setIdDetalle(int idDetalle)  { this.idDetalle = idDetalle; }

    public int getIdVenta()               { return idVenta; }
    public void setIdVenta(int idVenta)   { this.idVenta = idVenta; }

    public int getIdProducto()                  { return idProducto; }
    public void setIdProducto(int idProducto)   { this.idProducto = idProducto; }

    public String getNombreProducto()                    { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidad()               { return cantidad; }
    public void setCantidad(int cantidad)  { this.cantidad = cantidad; }

    public double getPrecioUnitario()                  { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal()               { return subtotal; }
    public void setSubtotal(double subtotal)  { this.subtotal = subtotal; }

    // ─── toString ────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "  Detalle [Producto: %-20s | Cant: %3d | P.Unit: $%10.2f | Subtotal: $%10.2f]",
            nombreProducto != null ? nombreProducto : String.valueOf(idProducto),
            cantidad, precioUnitario, subtotal
        );
    }
}
