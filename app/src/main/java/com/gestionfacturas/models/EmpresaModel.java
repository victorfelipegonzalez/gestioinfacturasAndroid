package com.gestionfacturas.models;

import java.io.Serializable;

public class EmpresaModel implements Serializable {
    private long id_empresa;
    private String nombre_empresa;
    private String nif_empresa;
    private int telefono_empresa;
    private String direccion_empresa;
    private String ciudad_empresa;
    private int cp_empresa;
    private String pais_empresa;
    private String correo;
    private String nombreJefe;
    private String pwd;
    private String correoJefe;

    public EmpresaModel() {
    }

    public long getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(long id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombreEmpresa() {
        return nombre_empresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombre_empresa = nombreEmpresa;
    }

    public String getNif() {
        return nif_empresa;
    }

    public void setNif(String nif) {
        this.nif_empresa = nif;
    }

    public int getTelefono() {
        return telefono_empresa;
    }

    public void setTelefono(int telefono) {
        this.telefono_empresa = telefono;
    }

    public String getDireccion() {
        return direccion_empresa;
    }

    public void setDireccion(String direccion) {
        this.direccion_empresa = direccion;
    }

    public String getCiudad() {
        return ciudad_empresa;
    }

    public void setCiudad(String ciudad) {
        this.ciudad_empresa = ciudad;
    }

    public int getCp() {
        return cp_empresa;
    }

    public void setCp(int cp) {
        this.cp_empresa = cp;
    }

    public String getPais() {
        return pais_empresa;
    }

    public void setPais(String pais) {
        this.pais_empresa = pais;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreJefe() {
        return nombreJefe;
    }

    public void setNombreJefe(String nombreJefe) {
        this.nombreJefe = nombreJefe;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCorreoJefe() {
        return correoJefe;
    }

    public void setCorreoJefe(String correoJefe) {
        this.correoJefe = correoJefe;
    }
}
