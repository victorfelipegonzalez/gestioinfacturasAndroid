package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;


public class RegistroClienteActivity extends AppCompatActivity {
    private EditText nombre,correo,telefono,nif;
    private ClienteModel cliente;
    private EmpleadoModel empleado;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        nombre = findViewById(R.id.et_nombreClienteFactura);
        correo = findViewById(R.id.et_correoCliente);
        telefono = findViewById(R.id.et_telefonoCliente);
        nif = findViewById(R.id.et_nifCliente);
        cliente = (ClienteModel) getIntent().getSerializableExtra("CLIENTE");
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        if(cliente.getId_cliente()>0){
            nombre.setText(cliente.getNombre_cliente());
            nif.setText(cliente.getNif_cliente());
            telefono.setText(String.valueOf(cliente.getTelefono_cliente()));
            correo.setText(cliente.getCorreo_cliente());
        }
    }
    //Método que nos envia a la activity de registro dirección cliente
    // si los  datos son validados
    public void registroDireccionCliente(View v){
        if(comprobarDatos()){
            cliente.setNombre_cliente(String.valueOf(nombre.getText()));
            cliente.setNif_cliente(String.valueOf(nif.getText()).toLowerCase());
            cliente.setTelefono_cliente((Integer.valueOf(String.valueOf(telefono.getText()))));
            cliente.setCorreo_cliente(String.valueOf(correo.getText()).toLowerCase());
            //reiniciarDatos();
            Intent intent = new Intent(this, RegistroDireccionClienteActivity.class);
            intent.putExtra("CLIENTE",cliente);
            intent.putExtra("EMPLEADO",empleado);
            startActivityForResult(intent,REQUEST_CODE);
        }
    }
    // Método para reniciar los campos
    public void reiniciarDatos(){
        nombre.setText(null);
        nombre.setHint(getResources().getString(R.string.nombre));
        correo.setText(null);
        correo.setHint(getResources().getString(R.string.correo_electronico));
        telefono.setText(null);
        telefono.setHint(getResources().getString(R.string.telefono));
        nif.setText(null);
        nif.setHint(getResources().getString(R.string.dni_nif));
    }

    // Método para comprobar los datos
    public boolean comprobarDatos(){
        if(nombre.getText().toString().isEmpty()){
            Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
            return false;
        } else if (nif.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un DNI / NIF",Toast.LENGTH_SHORT).show();
            return false;
        } else if (telefono.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un teléfono",Toast.LENGTH_SHORT).show();
            return false;
        } else if (correo.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debe introducir un correo",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(isValidEmail(String.valueOf(correo.getText()))) {
                if(telefono.getText().length() > 9){
                    Toast.makeText(this,"Número de teléfono\ndemasiado largo",Toast.LENGTH_SHORT).show();
                    return false;
                }else{
                    return true;
                }
            }else{
                Toast.makeText(this,"Correo electrónico no valido",Toast.LENGTH_SHORT).show();
                return false;
            }

        }
    }
    //Método para volver a la activity anterior
    public void volverRegistroCliente(View v){
        finish();
    }
    // Método para validar el patrón del email
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // Método que se queda a la espera de que se cierre la activity que hemos abierto desde
    // esta activity, comprueba el resultado obtenido y actua conforme a ello
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}