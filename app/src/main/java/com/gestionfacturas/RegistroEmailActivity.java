package com.gestionfacturas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.EmpresaModel;
import com.gestionfacturas.models.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroEmailActivity extends AppCompatActivity {
    private Button registro,volver;
    private EditText email,passwd,confimPasswd,nombreUsuario;
    private EmpresaModel empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_email);
        registro = findViewById(R.id.bt_finRegistro);
        volver  = findViewById(R.id.bt_volverRegistroEmail);
        email = findViewById(R.id.et_correoEmpresa);
        passwd = findViewById(R.id.et_passwdEmpresa);
        confimPasswd = findViewById(R.id.et_confirmarPasswdEmpresa);
        nombreUsuario = findViewById(R.id.et_nombreUsuario);
        empresa = (EmpresaModel) getIntent().getSerializableExtra("EMPRESA");


    }
    // Método para volver a la activity anterior
    public void vovlerRegistroDireccion(View v){
        finish();
    }
    //Método que envia la empresa a la API
    // si los  datos son validados
    public void finRegistro(View v){
        if(comprobarDatos()){
            try{
                crearEmpresa(String.valueOf(email.getText()), String.valueOf(passwd.getText()));
            }catch (NullPointerException e) {
                email.setText(null);
                email.setHint(getResources().getString(R.string.correo_electronico));
                Toast.makeText(this,"Correo electrónico erróneo",Toast.LENGTH_SHORT).show();
            }

        }
    }

    // Validación de campos
    public boolean comprobarDatos(){
        if(nombreUsuario.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
            return false;
        }else if(email.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un correo",Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwd.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir una contraseña",Toast.LENGTH_SHORT).show();
            return false;
        } else if (confimPasswd.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe confirmar la contraseña",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(passwd.getText().length() >= 6){
                if(passwd.getText().toString().equals(confimPasswd.getText().toString())){
                    if(isValidEmail(String.valueOf(email.getText()))){
                        empresa.setCorreoJefe(String.valueOf(email.getText()));
                        empresa.setNombreJefe(String.valueOf(nombreUsuario.getText()));
                        empresa.setPwd(String.valueOf(passwd.getText()));
                        return true;
                    }else{
                        Toast.makeText(this,"Correo electrónico erroneo",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(this,"No coinciden las contraseñas",Toast.LENGTH_SHORT).show();
                    confimPasswd.setText(null);
                    confimPasswd.setHint(getResources().getString(R.string.confirmar_contrasena));
                    return false;
                }
            }else{
                Toast.makeText(this,"La contraseña debe tener\nun minimo de 6 caracteres ",Toast.LENGTH_SHORT).show();
                confimPasswd.setText(null);
                confimPasswd.setHint(getResources().getString(R.string.confirmar_contrasena));
                passwd.setText(null);
                passwd.setHint(getResources().getString(R.string.contrasena));
                return false;
            }
        }
    }

    // Método para comprobar el patron de correo electrónico
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para conectarse a la API e introducir los datos en la base de datos PostgreSQL
    public void crearEmpresa(String emailEmpresa,String passwordEmpresa){
        try {
            ApiService apiService = APIConnection.getApiService();
            Call<ResponseModel> call = apiService.insertarEmpresa(empresa);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> callResponse, Response<ResponseModel> response) {
                    if (response.isSuccessful()&& response.body() != null) {
                        ResponseModel responseModel = response.body();
                        if(responseModel.getSuccess()==0){
                            Toast.makeText(RegistroEmailActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            Toast.makeText(RegistroEmailActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistroEmailActivity.this, "API sin respuesta"+response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(RegistroEmailActivity.this, "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }


}