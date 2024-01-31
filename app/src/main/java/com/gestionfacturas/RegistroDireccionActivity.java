package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.models.EmpresaModel;

public class RegistroDireccionActivity extends AppCompatActivity {
    Button siguiente,volver;
    EditText direccion,cp,ciudad,pais;
    EmpresaModel empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_direccion);
        siguiente = findViewById(R.id.bt_registroDireccionEmpresa);
        volver = findViewById(R.id.bt_volverRegistroDireccionEmpresa);
        direccion = findViewById(R.id.et_direcionEmpresa);
        cp = findViewById(R.id.et_cpEmpresa);
        ciudad = findViewById(R.id.et_ciudadEmpresa);
        pais = findViewById(R.id.et_paisEmpresa);
        empresa = (EmpresaModel) getIntent().getSerializableExtra("EMPRESA");
    }
    public void volverRegistroDatos(View v){
        finish();
    }
    public void registroEmail(View v){
        if(comprobarDatos()){
            Intent intent = new Intent(this,RegistroEmailActivity.class);
            intent.putExtra("EMPRESA",empresa);
            startActivity(intent);
        }

    }
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
                reiniciarDatos();
                return true;
            }else{
                Toast.makeText(this, "Código Postal\ndemasiado largo", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
    }
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
}