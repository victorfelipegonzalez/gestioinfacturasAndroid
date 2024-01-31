package com.gestionfacturas.api;

import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.EmpresaModel;
import com.gestionfacturas.models.FacturaModel;
import com.gestionfacturas.models.ProductoModel;
import com.gestionfacturas.models.ResponseModel;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/api/clientes/all/{id_empresa}")
    Call<ResponseModel> obtenerClientes(@Path("id_empresa") long id_empresa);
    @POST("/api/clientes/insertar")
    Call<ResponseModel> insertarCliente(@Body ClienteModel nuevoCiente);

    @POST("/api/empresas/insertar")
    Call<ResponseModel> insertarEmpresa(@Body EmpresaModel nuevaEmpresa);

    @GET("/api/empleados/buscarCorreo/{correoEmpleado}")
    Call<ResponseModel> obtenerEmpleado(@Path("correoEmpleado") String correoEmpleado);
    @GET("/api/empleados/all/{id_empresa}")
    Call<ResponseModel> obtenerEmpleados(@Path("id_empresa") long id_empresa);
    @POST("/api/empleados/actualizarEmpleado")
    Call<ResponseModel> actualizarEmpleado(@Body EmpleadoModel empleadoActualizado);
    @POST("/api/empleados/insertar")
    Call<ResponseModel> insertarEmpleado(@Body EmpleadoModel nuevoEmpleado);

    @POST("/api/productos/insertar")
    Call<ResponseModel> insertarProducto(@Body ProductoModel nuevoProducto);
    @GET("/api/productos/all/{id_empresa}")
    Call<ResponseModel> obtenerProductos(@Path("id_empresa") long id_empresa);
    @POST("/api/facturas/insertar")
    Call<ResponseModel> insertarFactura(@Body FacturaModel nuevaFactura);
    @GET("/api/facturas/clientes/{id_empresa}/{id_cliente}")
    Call<ResponseModel> obtenerListaFacturasCliente(@Path("id_empresa") long id_empresa,@Path("id_cliente") long id_cliente);
    @GET("/api/facturas/empleados/{id_empresa}/{id_empleado}")
    Call<ResponseModel> obtenerListaFacturasEmpleado(@Path("id_empresa") long id_empresa,@Path("id_empleado") long id_empleado);
    @GET("/api/facturas/informes/{id_factura}")
    Call<ResponseModel> generarInformeFactura(@Path("id_factura")long id_factura);
    @GET("/api/facturas/estadisticasfacturas/{id_empresa}")
    Call<ResponseModel> generarInformeEstadisticas(@Path("id_empresa") long id_empresa);
    @GET("/api/facturas/aniosfacturas/{id_empresa}")
    Call<ResponseModel> obtenerListaAniosFacturas(@Path("id_empresa")long id_empresa);
    @GET("api/facturas/estadisticasanuales/{id_empresa}/{anio}")
    Call<ResponseModel> generarInformeAnual(@Path("id_empresa")long id_empresa,@Path("anio") int anio);
    @GET("api/facturas/estadisticasanualesclientes/{id_empresa}/{anio}")
    Call<ResponseModel> generarInformeAnualClientes(@Path("id_empresa")long id_empresa,@Path("anio") int anio);

}
