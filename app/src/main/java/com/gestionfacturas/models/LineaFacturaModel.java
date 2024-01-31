package com.gestionfacturas.models;

public class LineaFacturaModel {
    private long id_producto;
    private long id_factura;
    private long id_linea_factura;
    private int cantidad;
    private double precio;
    public LineaFacturaModel(){

    }

    public long getId_factura() {
        return id_factura;
    }

    public void setId_factura(long id_factura) {
        this.id_factura = id_factura;
    }

    public long getId_producto() {
        return id_producto;
    }

    public void setId_producto(long id_producto) {
        this.id_producto = id_producto;
    }

    public long getId_linea_factura() {
        return id_linea_factura;
    }

    public void setId_linea_factura(long id_linea_factura) {
        this.id_linea_factura = id_linea_factura;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String toString() {
        return "ID_PRODUCTO: "+this.id_producto+
                " CANTIDAD: "+this.cantidad+
                " PRECIO: "+this.precio;
    }
}
