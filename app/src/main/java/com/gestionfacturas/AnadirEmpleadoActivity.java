package com.gestionfacturas;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnadirEmpleadoActivity extends AppCompatActivity {
    EmpleadoModel empleado;
    EditText correo;
    Button guardar,volver;
    String[] lista;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_empleado);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        correo = findViewById(R.id.et_anadir_correo);
        guardar = findViewById(R.id.bt_anadirEmpleado);
        volver = findViewById(R.id.bt_volverAnadirEmpleado);
        spinner = findViewById(R.id.sp_tipo_empleado);
        lista = new String[]{EmpleadoModel.ROL_EMPLEADO,EmpleadoModel.ROL_ADMINISTRADOR};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lista);
        spinner.setAdapter(adapter);
    }

    public void anadirEmpleado(View v){
        if(comprobarDatos()){
            actualizarEmpleado();
        }
    }

    public void volverAnadirEmpleado(View v){
        finish();
    }
    public boolean comprobarDatos(){
        if(correo.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un correo",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if (isValidEmail(String.valueOf(correo.getText()))){
                return true;
            }else{
                Toast.makeText(this,"Correo eletrónico incorrecto",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void actualizarEmpleado(){
        ApiService apiService = APIConnection.getApiService();
        EmpleadoModel empleadoActualizado = new EmpleadoModel();
        empleadoActualizado.setId_empresa(empleado.getId_empresa());
        empleadoActualizado.setCorreo(String.valueOf(correo.getText()));
        empleadoActualizado.setTipo_empleado(String.valueOf(spinner.getSelectedItem()));
        Call<ResponseModel> call = apiService.actualizarEmpleado(empleadoActualizado);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess()==0){
                        finish();
                        Toast.makeText(AnadirEmpleadoActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        correo.requestFocus();
                        Toast.makeText(AnadirEmpleadoActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AnadirEmpleadoActivity.this, "API sin respuesta"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("API Call", "Error en la llamada API", t);
                Toast.makeText(AnadirEmpleadoActivity.this, "Error al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}