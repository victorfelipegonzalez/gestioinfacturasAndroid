package com.gestionfacturas;
import androidx.annotation.NonNull;
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
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.EmpresaModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button entrar,registrase;
    private EditText emailInicio,passwdInicio;
    private FirebaseAuth mAuth;
    private Gson gson;
    private EmpleadoModel empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        entrar = findViewById(R.id.bt_entrar);
        registrase = findViewById(R.id.bt_registrarse);
        emailInicio = findViewById(R.id.et_correoInicio);
        passwdInicio = findViewById(R.id.et_passwdInicio);
        gson = new Gson();
        empleado= new EmpleadoModel();
    }
    public void iniciarSesion(View v){
        if(comprobarDatos()){
            String emailLower = String.valueOf(emailInicio.getText()).toLowerCase();
            loginUser(emailLower,String.valueOf(passwdInicio.getText()));
        }
    }
    // Método para entrar en la aplicación
    public void loginUser(String email, String passwd){
        // Comprobamos credenciales de acceso
        mAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    obtenerEmpleado();
                }else{
                    Toast.makeText(LoginActivity.this,"Correo o contraseña\nINCORRECTAS",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Error al iniciar sesión",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrarse(View view) {
        reiniciarDatos();
        startActivity(new Intent(this, RegistroActivity.class));
    }
    public void registrarEmpleado(View v) {
        reiniciarDatos();
        startActivity(new Intent(this, RegistroEmpleadoActivity.class));
    }
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
    public void reiniciarDatos(){
        emailInicio.setText(null);
        passwdInicio.setText(null);
        emailInicio.setHint(getResources().getString(R.string.correo_electronico));
        passwdInicio.setHint(getResources().getString(R.string.contrasena));
    }
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
                                empleado = empleadoDevuelto;
                                Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("EMPLEADO",empleado);
                                startActivity(intent);
                                reiniciarDatos();
                                Toast.makeText(LoginActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
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