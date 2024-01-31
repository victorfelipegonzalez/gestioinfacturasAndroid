package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.adapters.AdapterListaMateriales;
import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.FacturaModel;
import com.gestionfacturas.models.LineaFacturaModel;
import com.gestionfacturas.models.ProductoModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearLineasFacturaActivity extends AppCompatActivity {
    private FacturaModel factura;
    private Button volver,crearFactura,anadirMaterial;
    private ArrayList<LineaFacturaModel> listaLineasFactura;
    private List<ProductoModel> listaProductos;
    private ArrayList<ProductoModel> productosLista;
    private Gson gson;
    private ArrayList<String> nombreProductos;
    private AutoCompleteTextView nombreMaterial;
    private EditText cantidadMaterial;
    private RecyclerView listaDinamica;
    private AdapterListaMateriales adapter;
    private long idEmpresa;
    private EmpleadoModel empleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lineas_factura);
        volver = findViewById(R.id.bt_volver_crear_lineafactura);
        crearFactura = findViewById(R.id.bt_crear_factura);
        anadirMaterial = findViewById(R.id.bt_anadir_linea_factura);
        nombreMaterial = findViewById(R.id.et_nombreMaterial);
        listaDinamica = findViewById(R.id.rv_lineasPedido);
        cantidadMaterial = findViewById(R.id.et_cantidadMaterial);
        listaDinamica.setLayoutManager(new LinearLayoutManager(this));
        factura = (FacturaModel) getIntent().getSerializableExtra("FACTURA");
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        idEmpresa = empleado.getId_empresa();
        obtenerProductos();
        gson = new Gson();
        productosLista = new ArrayList<>();
        listaLineasFactura = new ArrayList<>();
        ArrayAdapter<String> nombres = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,nombreProductos);
        nombreMaterial.setAdapter(nombres);
        crearFactura.setVisibility(View.INVISIBLE);


    }
    
    // Método para hacer visible el boton de crear factura
    public void cambiarEstadoCrearFactura(){
        if(listaLineasFactura.size() > 0){
            crearFactura.setVisibility(View.VISIBLE);
        }else{
            crearFactura.setVisibility(View.INVISIBLE);
        }
    }
    //Método para añadir lineas de factura y almacenarlas en una lista
    public void crearlineaFactura(View v){
        if(comprobarCampos()){
            String busqueda = String.valueOf(nombreMaterial.getText());
            int cantidadProducto = Integer.valueOf(String.valueOf(cantidadMaterial.getText()));
            int contador = 0;
            if(busqueda.isEmpty()){
                Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
            }else{
                for(ProductoModel producto: listaProductos){
                    if(producto.getDescripcion().equals(busqueda)){
                        if(comprobarProductoEnLista(producto)){
                            Toast.makeText(this,"Material ya añadido a la lista",Toast.LENGTH_SHORT).show();
                        }else{
                            contador++;
                            LineaFacturaModel linea = new LineaFacturaModel();
                            linea.setId_producto(producto.getId_producto());
                            linea.setCantidad(cantidadProducto);
                            linea.setPrecio(producto.getPrecio());

                            listaLineasFactura.add(linea);
                            adapter.notifyItemInserted(listaLineasFactura.size() - 1);
                            limpiarCampos();
                            nombreMaterial.requestFocus();
                            cambiarEstadoCrearFactura();
                        }
                    }
                }
                if(contador == 0){
                    Toast.makeText(this,"Material no encontrado",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    // Método para crear la factura y enviarla a la API
    public void crearFacturaFinal(View v){
        if(listaLineasFactura.size() > 0){
            ApiService apiService = APIConnection.getApiService();
            factura.setLineasFactura(listaLineasFactura);
            Call call = apiService.insertarFactura(factura);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call <ResponseModel> callResponse, Response<ResponseModel> response) {
                    if(response.isSuccessful()){
                        ResponseModel responseModel = response.body();
                        if(responseModel.getSuccess()==0){
                            Intent intent = new Intent(CrearLineasFacturaActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("EMPLEADO",empleado);
                            startActivity(intent);
                            Toast.makeText(CrearLineasFacturaActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CrearLineasFacturaActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CrearLineasFacturaActivity.this, "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(CrearLineasFacturaActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(CrearLineasFacturaActivity.this, "La lista no puede estar vacia", Toast.LENGTH_SHORT).show();
        }

    }
    public void volverLineasFactura(View v){
        finish();
    }
    public void obtenerProductos(){
        try{
            nombreProductos = new ArrayList<>();
            // Establecer la conexión
            ApiService apiService = APIConnection.getApiService();
            // Obtener lista de clientes del servidor
            Call<ResponseModel> call = apiService.obtenerProductos(idEmpresa);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        ResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getSuccess() == 0) {
                            // Obtener los datos de la respuesta obtenida por el servidor
                            String stringProductos= gson.toJson(responseModel.getData());
                            ProductoModel[] productoArray = gson.fromJson(stringProductos, ProductoModel[].class);
                            // Convertir el array a una lista
                            List<ProductoModel> datos = Arrays.asList(productoArray);
                            listaProductos = datos;
                            for(ProductoModel producto: datos) {
                                nombreProductos.add(producto.getDescripcion());
                                productosLista.add(producto);
                            }
                            adapter = new AdapterListaMateriales(productosLista,listaLineasFactura, position -> {

                            });
                            listaDinamica.setAdapter(adapter);

                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("ERROR","Error en la respuesta: "+ errorBody);
                                Toast.makeText(CrearLineasFacturaActivity.this, "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            };
                        }
                    } else {
                        Toast.makeText(CrearLineasFacturaActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(CrearLineasFacturaActivity.this, "Error en la llamada API: ", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean comprobarProductoEnLista(ProductoModel producto){
        boolean repetido = false;
        for(int i = 0;i< listaLineasFactura.size();i++){
            if(listaLineasFactura.get(i).getId_producto() == producto.getId_producto() ){
                repetido = true;
            }
        }
        return repetido;
    }
    public void limpiarCampos(){
        nombreMaterial.setText(null);
        cantidadMaterial.setText(null);
    }
    public boolean comprobarCampos(){
        if(nombreMaterial.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir el nombre\n de un material",Toast.LENGTH_SHORT).show();
            return false;
        }else if(cantidadMaterial.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir la cantidad\n de un material",Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.valueOf(String.valueOf(cantidadMaterial.getText()))<=0){
            Toast.makeText(this,"La cantidad tiene que ser\n superior a 0",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}