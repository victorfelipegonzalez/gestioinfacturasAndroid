package com.gestionfacturas.models;

import java.io.Serializable;

public class EmpleadoModel implements Serializable {
    public static final String ROL_ADMINISTRADOR = "JEFE";
    public static final String ROL_EMPLEADO= "EMPLEADO";
    private long id_empleado;
    private String nombre_empleado;
    private String correo;
    private String tipo_empleado;
    public long id_empresa;
    private String password;

    public EmpleadoModel() {
    }

    public long getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(long id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo_empleado() {
        return tipo_empleado;
    }

    public void setTipo_empleado(String tipo_empleado) {
        this.tipo_empleado = tipo_empleado;
    }

    public long getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(long id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
