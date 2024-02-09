package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.models.EmpresaModel;

public class RegistroActivity extends AppCompatActivity {
    private Button siguiente,volver;
    private EditText nombre,nif,telefono;
    private EmpresaModel empresa;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        siguiente = findViewById(R.id.bt_anadir_cliente_factura);
        volver = findViewById(R.id.bt_volverDatosEmpresa);
        nombre = findViewById(R.id.et_NombreEmpresa);
        nif = findViewById(R.id.et_nifEmpresa);
        telefono = findViewById(R.id.et_telefonoEmpresa);
        empresa = new EmpresaModel();
    }
    public void registroDireccion(View v){
        if(comprobarDatos()){
            Intent intent = new Intent(this, RegistroDireccionActivity.class);
            intent.putExtra("EMPRESA",empresa);
            startActivityForResult(intent, REQUEST_CODE);
        }

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
    public void volverInicio(View v){
        finish();
    }
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
        }else {
            if(telefono.getText().toString().length() <= 9){
                empresa.setNombreEmpresa(String.valueOf(nombre.getText()));
                empresa.setNif(String.valueOf(nif.getText()).toLowerCase());
                empresa.setTelefono(Integer.valueOf(String.valueOf(telefono.getText())));
                reiniciarDatos();
                return true;
            }else{
                Toast.makeText(this,"Número de teléfono\ndemasiado largo",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }
    public void reiniciarDatos(){
        nombre.setText(null);
        nif.setText(null);
        telefono.setText(null);
        nombre.setHint(getResources().getString(R.string.nombre));
        nif.setHint(getResources().getString(R.string.nif));
        telefono.setHint(getResources().getString(R.string.telefono));
    }
}