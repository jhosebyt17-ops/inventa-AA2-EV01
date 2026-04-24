package com.inventa.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Venta
 * Representa la entidad Venta (encabezado) del sistema Inventa.
 *
 * @author Inventa Team
 * @version 1.0
 */
public class Venta {

    private int          idVenta;
    private int          idCliente;
    private String       nombreCliente;   // Campo auxiliar para mostrar en consultas
    private String       fechaVenta;
    private double       total;
    private String       estado;
    private List<DetalleVenta> detalles;

    // ─── Constructores ───────────────────────────────────────────────

    /** Constructor vacío */
    public Venta() {
        this.detalles = new ArrayList<>();
        this.estado   = "ACTIVA";
    }

    /** Constructor con ID */
    public Venta(int idVenta, int idCliente, String fechaVenta,
                 double total, String estado) {
        this.idVenta    = idVenta;
        this.idCliente  = idCliente;
        this.fechaVenta = fechaVenta;
        this.total      = total;
        this.estado     = estado;
        this.detalles   = new ArrayList<>();
    }

    // ─── Getters y Setters ───────────────────────────────────────────

    public int getIdVenta()               { return idVenta; }
    public void setIdVenta(int idVenta)   { this.idVenta = idVenta; }

    public int getIdCliente()                { return idCliente; }
    public void setIdCliente(int idCliente)  { this.idCliente = idCliente; }

    public String getNombreCliente()                   { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getFechaVenta()                { return fechaVenta; }
    public void setFechaVenta(String fechaVenta) { this.fechaVenta = fechaVenta; }

    public double getTotal()             { return total; }
    public void setTotal(double total)   { this.total = total; }

    public String getEstado()              { return estado; }
    public void setEstado(String estado)   { this.estado = estado; }

    public List<DetalleVenta> getDetalles()                    { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles)       { this.detalles = detalles; }
    public void agregarDetalle(DetalleVenta detalle)           { this.detalles.add(detalle); }

    // ─── toString ────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "Venta [ID: %d | Cliente: %s | Fecha: %s | Total: $%.2f | Estado: %s]",
            idVenta, nombreCliente != null ? nombreCliente : String.valueOf(idCliente),
            fechaVenta, total, estado
        );
    }
}
