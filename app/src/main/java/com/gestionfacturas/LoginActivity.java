package com.gestionfacturas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button entrar,registrase;
    private EditText emailInicio,passwdInicio;
    private Gson gson;
    private EmpleadoModel empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        entrar = findViewById(R.id.bt_entrar);
        registrase = findViewById(R.id.bt_registrarse);
        emailInicio = findViewById(R.id.et_correoInicio);
        passwdInicio = findViewById(R.id.et_passwdInicio);
        gson = new Gson();
        empleado= new EmpleadoModel();
    }
    // Método que llama al metodo de login si los campos han sido validados
    public void iniciarSesion(View v){
        if(comprobarDatos()){
            loginUser();
        }
    }
    // Método para entrar en la aplicación
    public void loginUser(){
        ApiService apiService = APIConnection.getApiService();
        String emailEntrar = String.valueOf(emailInicio.getText()).toLowerCase();
        String pwdEntrar = String.valueOf(passwdInicio.getText());
        Call<ResponseModel> call = apiService.login(emailEntrar,pwdEntrar);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try{
                    if (response.isSuccessful()) {
                        ResponseModel responseModel = response.body();
                        if (responseModel.getSuccess() == 0) {
                            // Obtener los datos de la respuesta obtenida por el servidor
                            String stringEmpleado= gson.toJson(responseModel.getData());
                            EmpleadoModel empleadoDevuelto = gson.fromJson(stringEmpleado, EmpleadoModel.class);
                            // Establecemos al empleado que hemos recibido en nuestra variable
                            empleado = empleadoDevuelto;
                            // Enviamos el empleado recibido a la siguiente activity
                            Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("EMPLEADO",empleado);
                            startActivity(intent);
                            reiniciarDatos();
                            //Toast.makeText(LoginActivity.this,"Bienvenido "+empleado.getNombre_empleado(),Toast.LENGTH_SHORT).show();
                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            Toast.makeText(LoginActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Log.e("ERROR CON LOS DATOS", e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("API Call", "Error en la llamada API", t);
                Toast.makeText(LoginActivity.this, "Error al conectar con el servidor ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método que nos envia a la activity de registro
    public void registrarse(View view) {
        reiniciarDatos();
        startActivity(new Intent(this, RegistroActivity.class));
    }
    // Método que nos envia a la activity de registro de empleados
    public void registrarEmpleado(View v) {
        reiniciarDatos();
        startActivity(new Intent(this, RegistroEmpleadoActivity.class));
    }
    //Método para validar los campos
    public boolean comprobarDatos(){
        if(emailInicio.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un email",Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwdInicio.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir una contraseña",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(passwdInicio.getText().toString().length() < 6){
                Toast.makeText(this,"Contraseña demasiado corta",Toast.LENGTH_SHORT).show();
                return false;
            }else{
                return true;
            }
        }
    }
    // Método para reiniciar los campos
    public void reiniciarDatos(){
        emailInicio.setText(null);
        passwdInicio.setText(null);
        emailInicio.setHint(getResources().getString(R.string.correo_electronico));
        passwdInicio.setHint(getResources().getString(R.string.contrasena));
    }
    // Método que nos devuelve un empleado a partir del email obtenido
    public void obtenerEmpleado(){
        ApiService apiService = APIConnection.getApiService();
        String emailEntrar = String.valueOf(emailInicio.getText()).toLowerCase();
        Call<ResponseModel> call = apiService.obtenerEmpleado(emailEntrar);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    try{
                        if (response.isSuccessful()) {
                            ResponseModel responseModel = response.body();
                            if (responseModel.getSuccess() == 0) {
                                // Obtener los datos de la respuesta obtenida por el servidor
                                String stringEmpleado= gson.toJson(responseModel.getData());
                                EmpleadoModel empleadoDevuelto = gson.fromJson(stringEmpleado, EmpleadoModel.class);
                                // Establecemos al empleado que hemos recibido en nuestra variable
                                empleado = empleadoDevuelto;
                                // Enviamos el empleado recibido a la siguiente activity
                                Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("EMPLEADO",empleado);
                                startActivity(intent);
                                reiniciarDatos();
                                //Toast.makeText(LoginActivity.this,"Bienvenido "+empleado.getNombre_empleado(),Toast.LENGTH_SHORT).show();
                            } else {
                                // Manejar la respuesta exitosa pero sin datos o con un código de error
                                Toast.makeText(LoginActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e) {
                        Log.e("ERROR CON LOS DATOS", e.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(LoginActivity.this, "Error al conectar con el servidor ", Toast.LENGTH_SHORT).show();
                }
            });
    }
}