package com.gestionfacturas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroEmpleadoActivity extends AppCompatActivity {
    Button volver,guardar;
    EditText nombre,correo,passwd,confimPasswd;
    EmpleadoModel empleado;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empleado);
        mAuth = FirebaseAuth.getInstance();
        volver = findViewById(R.id.bt_volverEmpleado);
        guardar = findViewById(R.id.bt_guardarDatosEmpleado);
        nombre = findViewById(R.id.et_nombreEmpleado);
        correo = findViewById(R.id.et_correoEmpleado);
        passwd = findViewById(R.id.et_passwdEmpleado);
        confimPasswd = findViewById(R.id.et_confirmPasswdEmpleado);
        empleado = new EmpleadoModel();
    }
    // Método para registar los datos del empleado en la base de datos PostgreSQL y en FireBase
    public void guardarDatosEmpleado(View v){
        if(comprobarDatos()){
            try {
                ApiService apiService = APIConnection.getApiService();
                Call<ResponseModel> call = apiService.insertarEmpleado(empleado);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            ResponseModel responseModel = response.body();
                            if(responseModel.getSuccess()==0){
                                String correoLower = String.valueOf(correo.getText()).toLowerCase();
                                registrarDatos(correoLower,String.valueOf(passwd.getText()),responseModel);
                            }else{
                                Toast.makeText(RegistroEmpleadoActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("TEST", response.toString());
                            Toast.makeText(RegistroEmpleadoActivity.this, "API sin respuesta"+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("API Call", "Error en la llamada API", t);
                        Toast.makeText(RegistroEmpleadoActivity.this, "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (NullPointerException e) {
                correo.setText(null);
                correo.setHint(getResources().getString(R.string.correo_electronico));
                Toast.makeText(this,"Correo electrónico erróneo",Toast.LENGTH_SHORT).show();
            }catch (Exception e) {
                String error = e.getMessage();
            }
        }
    }
    // Método para comprobar los datos del formulario
    public boolean comprobarDatos(){
        if(nombre.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
            return false;
        } else if (correo.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un correo",Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwd.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir una contraseña",Toast.LENGTH_SHORT).show();
            return false;
        } else if (confimPasswd.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe confirmar la contraseña",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (passwd.getText().toString().length() < 6) {
                Toast.makeText(this, "La contraseña debe tener\nun minimo de 6 caracteres ", Toast.LENGTH_SHORT).show();
                confimPasswd.setText(null);
                confimPasswd.setHint(getResources().getString(R.string.confirmar_contrasena));
                passwd.setText(null);
                passwd.setHint(getResources().getString(R.string.contrasena));
                return false;
            } else {
                if (passwd.getText().toString().equals(confimPasswd.getText().toString())) {
                    if(isValidEmail(String.valueOf(correo.getText()))){
                        empleado.setNombre_empleado(String.valueOf(nombre.getText()));
                        empleado.setCorreo(String.valueOf(correo.getText()));
                        empleado.setTipo_empleado(empleado.ROL_EMPLEADO);
                        return true;
                    }else{
                        Toast.makeText(this,"Correo incorrecto",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else {
                    Toast.makeText(this, "No coinciden las contraseñas", Toast.LENGTH_SHORT).show();
                    confimPasswd.setText(null);
                    confimPasswd.setHint(getResources().getString(R.string.confirmar_contrasena));
                    return false;
                }
            }
        }
    }

    // Método para registrar los datos en FIreBase
    public void registrarDatos(String emailEmpleado, String passwordEmpleado,ResponseModel responseModel){
        mAuth.createUserWithEmailAndPassword(emailEmpleado,passwordEmpleado).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                finish();
                Toast.makeText(RegistroEmpleadoActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroEmpleadoActivity.this,"Error al registrar",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void volverRegistroEmpleado(View v){
        finish();
    }
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}