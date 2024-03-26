package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroDireccionClienteActivity extends AppCompatActivity {
    private Button volver,guardar;
    private EditText direccion,cp,ciudad,pais;
    private ClienteModel cliente;
    private EmpleadoModel empleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_direccion_cliente);
        volver = findViewById(R.id.bt_volverRegistroDireccionCliente);
        guardar = findViewById(R.id.bt_guardarDatosCliente);
        direccion = findViewById(R.id.et_direccionCliente);
        cp = findViewById(R.id.et_cpCliente);
        ciudad = findViewById(R.id.et_ciudadCliente);
        pais = findViewById(R.id.et_paisCliente);
        cliente = (ClienteModel) getIntent().getSerializableExtra("CLIENTE");
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEAD");
    }
    //Método que envia el cliente a la API
    // si los  datos son validados
    public void guardarDatosCliente(View v) {
        if(comprobarDatos()){
            insertarCliente(cliente);
        }
    }
    // Validar los campos
    public boolean comprobarDatos() {
        if (direccion.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe introducir una dirección", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cp.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe introducir un código postal", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ciudad.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe introducir una ciudad", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pais.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe introducir un país", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(cp.getText().length() > 5) {
                Toast.makeText(this, "Código postal \ndemasiado largo", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                cliente.setDireccion_cliente(String.valueOf(direccion.getText()));
                cliente.setCp_cliente(Integer.valueOf(String.valueOf(cp.getText())));
                cliente.setCiudad_cliente(String.valueOf(ciudad.getText()));
                cliente.setPais_cliente(String.valueOf(pais.getText()));
                reiniciarDatos();
                return true;
            }
        }

    }
    // Método para reniciar los campos
    public void reiniciarDatos(){
        direccion.setText(null);
        cp.setText(null);
        ciudad.setText(null);
        pais.setText(null);
        direccion.setHint(getResources().getString(R.string.direccion));
        cp.setHint(getResources().getString(R.string.codigo_postal));
        ciudad.setHint(getResources().getString(R.string.ciudad));
        pais.setHint(getResources().getString(R.string.pais));
    }
    // Método para volver a la activity anterior
    public void volverRegistroDireccionClientes(View v){
        finish();
    }
    // Método para insertar Clientes
    public void insertarCliente(ClienteModel nuevoCliente) {
        ApiService apiService = APIConnection.getApiService();
        // Realizamos la llamada para insertar el cliente
        Call<ResponseModel> call = apiService.insertarCliente(nuevoCliente);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try{
                    if (response.isSuccessful()) {
                        ResponseModel responseModel = response.body();
                        if(responseModel.getSuccess()==0){
                            // Clientes registrado
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            Toast.makeText(RegistroDireccionClienteActivity.this,responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            // Si ha salido bien pero no se ha podido registrar por algun error con los datos
                            Toast.makeText(RegistroDireccionClienteActivity.this,responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Cliente no registrado
                        Toast.makeText(RegistroDireccionClienteActivity.this,"Error al guardar cliente", Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){
                    Log.e("ERROR","Error al recibir datos del servidor");
                    Toast.makeText(RegistroDireccionClienteActivity.this,"Error al guardar cliente", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Si hay error en la llamada
                Toast.makeText(RegistroDireccionClienteActivity.this,"Error al conectarse con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}