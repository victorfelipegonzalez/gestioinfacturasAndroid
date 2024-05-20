package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.EmpresaModel;

public class ActualizarEmpresaActivity extends AppCompatActivity {
    private EmpleadoModel empleado;
    private EmpresaModel empresa;
    private Button siguiente,volver;
    private EditText nombre,nif,telefono,correo;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_empresa);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        empresa = (EmpresaModel) getIntent().getSerializableExtra("EMPRESA");
        siguiente = findViewById(R.id.bt_siguiente_actualizar);
        volver = findViewById(R.id.bt_volverActualizarEmpresa);
        nombre = findViewById(R.id.et_NombreEmpresa);
        nif = findViewById(R.id.et_nifEmpresa);
        telefono = findViewById(R.id.et_telefonoEmpresa);
        correo = findViewById(R.id.et_correoEmpresa);
        cargarDatos();
    }

    // Método que se queda a la espera de que se cierre la activity que hemos abierto desde
    // esta activity, comprueba el resultado obtenido y actua conforme a ello
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
                empresa = (EmpresaModel) getIntent().getSerializableExtra("EMPRESA");
                Intent intent  = new Intent();
                intent.putExtra("EMPRESA",empresa);
                intent.putExtra("EMPLEADO",empleado);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
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
        }else if (correo.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un correo eléctonico",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(telefono.getText().toString().length() <= 9){
                empresa.setNombreEmpresa(String.valueOf(nombre.getText()));
                empresa.setNif(String.valueOf(nif.getText()).toLowerCase());
                empresa.setTelefono(Integer.valueOf(String.valueOf(telefono.getText())));
                empresa.setCorreo(String.valueOf(correo.getText()));
                return true;
            }else{
                Toast.makeText(this,"Número de teléfono\ndemasiado largo",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }
    //Método para pasar a la siguiente activity después de comprobar los campos
    public void actualizarDatosDireccion(View v){
        if(comprobarDatos()){
            Intent intent = new Intent(this, ActualizarDireccionEmpresaActivity.class);
            intent.putExtra("EMPLEADO",empleado);
            intent.putExtra("EMPRESA",empresa);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }
    //Método para volver a la activity anterior
    public void volverActualizarDatos(View v){
        finish();
    }

    //Método para cargar los datos de la empresa
    public void cargarDatos(){
        nombre.setText(empresa.getNombreEmpresa());
        nif.setText(empresa.getNif());
        telefono.setText(String.valueOf(empresa.getTelefono()));
        correo.setText(empresa.getCorreo());
    }

}