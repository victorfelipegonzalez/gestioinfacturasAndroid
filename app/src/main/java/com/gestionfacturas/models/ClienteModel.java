package com.gestionfacturas.models;


import java.io.Serializable;

public class ClienteModel implements Serializable {
    private long id_cliente;
    private String nombre_cliente;
    private String nif_cliente;
    private int telefono_cliente;
    private String direccion_cliente;
    private String ciudad_cliente;
    private int cp_cliente;
    private String pais_cliente;
    private String correo_cliente;
    private long id_empresa;

    public ClienteModel() {
    }

    public long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNif_cliente() {
        return nif_cliente;
    }

    public void setNif_cliente(String nif_cliente) {
        this.nif_cliente = nif_cliente;
    }

    public int getTelefono_cliente() {
        return telefono_cliente;
    }

    public void setTelefono_cliente(int telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

    public String getDireccion_cliente() {
        return direccion_cliente;
    }

    public void setDireccion_cliente(String direccion_cliente) {
        this.direccion_cliente = direccion_cliente;
    }

    public String getCiudad_cliente() {
        return ciudad_cliente;
    }

    public void setCiudad_cliente(String ciudad_cliente) {
        this.ciudad_cliente = ciudad_cliente;
    }

    public int getCp_cliente() {
        return cp_cliente;
    }

    public void setCp_cliente(int cp_cliente) {
        this.cp_cliente = cp_cliente;
    }

    public String getPais_cliente() {
        return pais_cliente;
    }

    public void setPais_cliente(String pais_cliente) {
        this.pais_cliente = pais_cliente;
    }

    public String getCorreo_cliente() {
        return correo_cliente;
    }

    public void setCorreo_cliente(String correo_cliente) {
        this.correo_cliente = correo_cliente;
    }

    public String toString() {
        return nombre_cliente;
    }

    public long getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(long id_empresa) {
        this.id_empresa = id_empresa;
    }
}
