package com.gestionfacturas.models;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FacturaModel implements Serializable {
    private long id_factura;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha;
    private List<LineaFacturaModel> lineasFactura;
    private long id_empleado;
    private long id_cliente;

    public FacturaModel() {
    }

    public long getId_factura() {
        return id_factura;
    }

    public void setId_factura(long id_factura) {
        this.id_factura = id_factura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<LineaFacturaModel> getLineasFactura() {
        return lineasFactura;
    }

    public void setLineasFactura(List<LineaFacturaModel> lineasFactura) {
        this.lineasFactura = lineasFactura;
    }

    public long getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(long id_empleado) {
        this.id_empleado = id_empleado;
    }

    public long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String toString() {
        return "FECHA: "+getFormattedFecha();
    }
    public String getFormattedFecha() {
        // Formatear la fecha según tus preferencias
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.fecha);
    }
}
