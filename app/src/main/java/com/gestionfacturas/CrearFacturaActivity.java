package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.FacturaModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearFacturaActivity extends AppCompatActivity {
    private Button volver,siguiente;
    private AutoCompleteTextView buscarCliente;
    private EmpleadoModel empleado;
    private List<ClienteModel> listaClientes;
    private Gson gson;
    private ArrayList<String> nombreClientes;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_factura);
        volver = findViewById(R.id.bt_volver_crear_factura);
        siguiente = findViewById(R.id.bt_anadir_cliente_factura);
        buscarCliente = findViewById(R.id.et_nombreClienteFactura);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        gson = new Gson();
        obtenerListaClientes();
        ArrayAdapter<String> nombres = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,nombreClientes);
        buscarCliente.setAdapter(nombres);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
    public void obtenerListaClientes(){
        try{
            listaClientes = null;
            nombreClientes = new ArrayList<>();
            // Establecer la conexión
            ApiService apiService = APIConnection.getApiService();
            // Obtener lista de clientes del servidor
            Call<ResponseModel> call = apiService.obtenerClientes(empleado.getId_empresa());
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        ResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getSuccess() == 0) {
                            // Obtener los datos de la respuesta obtenida por el servidor
                            String stringClientes= gson.toJson(responseModel.getData());
                            ClienteModel[] clienteArray = gson.fromJson(stringClientes, ClienteModel[].class);
                            // Convertir el array a una lista
                            List<ClienteModel> datos = Arrays.asList(clienteArray);
                            listaClientes = datos;
                            for(ClienteModel cliente: datos) {
                                nombreClientes.add(cliente.getNombre_cliente());
                            }
                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("ERROR","Error en la respuesta: "+ errorBody);
                                Toast.makeText(CrearFacturaActivity.this, "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            };
                        }
                    } else {
                        Toast.makeText(CrearFacturaActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(CrearFacturaActivity.this, "Error en la llamada API: ", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void volverCrearFactura(View v ){
        finish();
    }

    public void crearFactura(View v){
        FacturaModel factura = new FacturaModel();
        String busqueda = String.valueOf(buscarCliente.getText());
        int contador = 0;
        if(busqueda.isEmpty()){
            Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
        }else{
            for(ClienteModel cliente: listaClientes){
                if(cliente.getNombre_cliente().equals(busqueda)){
                    factura.setId_empleado(empleado.getId_empleado());
                    factura.setId_cliente(cliente.getId_cliente());
                    contador++;
                }
            }
            if(contador == 0){
                Toast.makeText(this,"Cliente no encontrado",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(this, CrearLineasFacturaActivity.class);
                intent.putExtra("FACTURA",factura);
                intent.putExtra("EMPLEADO",empleado);
                startActivityForResult(intent,REQUEST_CODE);
            }
        }
    }
}