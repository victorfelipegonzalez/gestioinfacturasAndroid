package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.EmpresaModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizarDireccionEmpresaActivity extends AppCompatActivity {
    private Button actualizar,volver;
    private EditText direccion,cp,ciudad,pais;
    private EmpresaModel empresa;
    private EmpleadoModel empleado;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_direccion_empresa);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        empresa = (EmpresaModel) getIntent().getSerializableExtra("EMPRESA");
        volver = findViewById(R.id.bt_volveractualizarDireccionEmpresa);
        actualizar = findViewById(R.id.bt_actualizarDireccionEmpresa);
        direccion = findViewById(R.id.et_actualizarDirecionEmpresa);
        cp = findViewById(R.id.et_actualizarCpEmpresa);
        ciudad = findViewById(R.id.et_actualizarCiudadEmpresa);
        pais = findViewById(R.id.et_actualizarPaisEmpresa);
        gson = new Gson();
        cargarDatosEmpresa();
    }

    // Método para comprobar los datos
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
            if(cp.getText().toString().length() <= 5){
                empresa.setDireccion(String.valueOf(direccion.getText()));
                empresa.setCp(Integer.valueOf(String.valueOf(cp.getText())));
                empresa.setCiudad(String.valueOf(ciudad.getText()));
                empresa.setPais(String.valueOf(pais.getText()));
                return true;
            }else{
                Toast.makeText(this, "Código Postal\ndemasiado largo", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
    }

    public void actualizarDatosEmpresa(View view){
        if(comprobarDatos()){
            actualizarDatos();
        }
    }
    //Método que manda los datos a la API para actualizarlos y devuelve los datos actualizados
    public void actualizarDatos(){
        ApiService apiService = APIConnection.getApiService();
        Call < ResponseModel> call = apiService.actualizarEmpresa(empresa);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel != null && responseModel.getSuccess() == 0){
                        String empresaJSON = gson.toJson(responseModel.getData());
                        empresa = gson.fromJson(empresaJSON, EmpresaModel.class);
                        volverActualizado(empresa);
                        Toast.makeText(ActualizarDireccionEmpresaActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ActualizarDireccionEmpresaActivity.this, "Correo eléctronico en uso", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ActualizarDireccionEmpresaActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
    //Método para volver a la activity anterio
    public void volverActualizarDatosEmpresa(View view) {
        finish();
    }

    //Método para volver en caso de que se haya actualizado los datos de la empresa
    public void volverActualizado(EmpresaModel empresaModel){
        Intent intent = new Intent();
        intent.putExtra("EMPRESA",empresaModel);
        intent.putExtra("EMPLEADO",empleado);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cargarDatosEmpresa(){
        direccion.setText(empresa.getDireccion());
        cp.setText(String.valueOf(empresa.getCp()));
        ciudad.setText(empresa.getCiudad());
        pais.setText(empresa.getPais());
    }
}