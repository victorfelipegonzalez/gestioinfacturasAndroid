package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.models.EmpresaModel;

public class RegistroActivity extends AppCompatActivity {
    private Button siguiente,volver;
    private EditText nombre,nif,telefono,correo;
    private EmpresaModel empresa;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        siguiente = findViewById(R.id.bt_siguienteDireccionResgistro);
        volver = findViewById(R.id.bt_volverEmpresa);
        nombre = findViewById(R.id.et_NombreEmpresa);
        nif = findViewById(R.id.et_nifEmpresa);
        telefono = findViewById(R.id.et_telefonoEmpresa);
        correo = findViewById(R.id.et_correoInscripcionEmpresa);
        empresa = new EmpresaModel();
    }

    //Método que nos envia a la activity de registro dirección
    // si los  datos son validados
    public void registroDireccion(View v){
        if(comprobarDatos()){
            Intent intent = new Intent(this, RegistroDireccionActivity.class);
            intent.putExtra("EMPRESA",empresa);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }
    // Método que se queda a la espera de que se cierre la activity que hemos abierto desde
    // esta activity, comprueba el resultado obtenido y actua conforme a ello
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
    //Método para volver a la activity anterior
    public void volverInicio(View v){
        finish();
    }
    //Método para validar los datos
    public boolean comprobarDatos(){
        if(nombre.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
            return false;
        } else if (nif.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un NIF",Toast.LENGTH_SHORT).show();
            return false;
        } else if (telefono.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un teléfono",Toast.LENGTH_SHORT).show();
            return false;
        }else if(correo.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un correo",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(telefono.getText().toString().length() <= 9){
                if(isValidEmail(String.valueOf(correo.getText()))){
                    empresa.setNombreEmpresa(String.valueOf(nombre.getText()));
                    empresa.setNif(String.valueOf(nif.getText()).toLowerCase());
                    empresa.setTelefono(Integer.valueOf(String.valueOf(telefono.getText())));
                    empresa.setCorreo(String.valueOf(correo.getText()));
                    reiniciarDatos();
                    return true;
                }else {
                    Toast.makeText(this,"Correo electrónico erroneo",Toast.LENGTH_SHORT).show();
                    return false;
                }

            }else{
                Toast.makeText(this,"Número de teléfono\ndemasiado largo",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }
    // Método para reniciar los campos
    public void reiniciarDatos(){
        nombre.setText(null);
        nif.setText(null);
        telefono.setText(null);
        nombre.setHint(getResources().getString(R.string.nombre));
        nif.setHint(getResources().getString(R.string.nif));
        telefono.setHint(getResources().getString(R.string.telefono));
    }
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}