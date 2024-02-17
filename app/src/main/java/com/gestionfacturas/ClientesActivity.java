package com.gestionfacturas;

import com.gestionfacturas.api.APIConnection;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.ResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;

public class ClientesActivity extends AppCompatActivity {
    private List<ClienteModel> lista;
    private Button volver;
    private AutoCompleteTextView busquedaClientes;
    private ListView listView;
    private Gson gson = new Gson();
    private long id_empresa;
    private ArrayList<String> nombreClientes;
    private ImageButton buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        volver = findViewById(R.id.bt_volver_crear_factura);
        listView = findViewById(R.id.list_listaClientes);
        busquedaClientes = findViewById(R.id.et_nombreClienteFactura);
        buscar = findViewById(R.id.ib_buscar_cliente);
        nombreClientes = new ArrayList<>();
        id_empresa = getIntent().getLongExtra("IDEMPRESA",0);
        obtenerListaClientes();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mostrarCliente(lista.get(position));
            }
        });
        ArrayAdapter<String> nombres = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,nombreClientes);
        busquedaClientes.setAdapter(nombres);
    }

    // Método para rellenar la lista de clientes
    public void obtenerListaClientes(){
        try{
            // Establecer la conexión
            ApiService apiService = APIConnection.getApiService();
            // Obtener lista de clientes del servidor
            Call<ResponseModel> call = apiService.obtenerClientes(id_empresa);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        ResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getSuccess() == 0) {
                            // Obtener los datos de la respuesta obtenida por el servidor
                            String stringClientes= gson.toJson(responseModel.getData());
                            ClienteModel[] clienteArray = gson.fromJson(stringClientes, ClienteModel[].class);
                            // Convertir el array a una lista
                            List<ClienteModel> datos = Arrays.asList(clienteArray);
                            lista = datos;
                            for(ClienteModel cliente: datos) {
                                nombreClientes.add(cliente.getNombre_cliente());
                            }
                            // Actualizar la lista de clientes con los datos obtenidos
                            ArrayAdapter adapter = new ArrayAdapter<>(ClientesActivity.this, android.R.layout.simple_list_item_1,datos);
                            listView.setAdapter(adapter);
                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("ERROR","Error en la respuesta: "+ errorBody);
                                Toast.makeText(ClientesActivity.this, "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(ClientesActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(ClientesActivity.this, "Error en la llamada API: ", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    // Método para mostrar la información de un cliente
    private void mostrarCliente(ClienteModel cliente) {
        String mensaje = "Nombre: "+cliente.getNombre_cliente()+
                        "\nDNI: "+cliente.getNif_cliente()+
                        "\nTeléfono: "+cliente.getTelefono_cliente()+
                        "\nCorreo: "+cliente.getCorreo_cliente()+
                        "\nDirección: "+cliente.getDireccion_cliente()+
                        "\nCiudad: "+cliente.getCiudad_cliente()+
                        "\nCP: "+cliente.getCp_cliente()+
                        "\nPais: "+cliente.getPais_cliente();

        // Se crea un TextView para mostrar el mensaje
        TextView textView = new TextView(this);
        textView.setText(mensaje);
        textView.setTextSize(16);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        // Reconocer automáticamente teléfonos y correos electrónicos como enlaces
        Linkify.addLinks(textView, Linkify.PHONE_NUMBERS | Linkify.EMAIL_ADDRESSES);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CLIENTE")
                .setView(textView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cierra el diálogo
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void volverListaClientes(View v){
        finish();
    }

    public void bucarCliente(View v){
        String busqueda = String.valueOf(busquedaClientes.getText());
        int contador = 0;
        if(busqueda.isEmpty()){
            Toast.makeText(this,"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
        }else{
            for(ClienteModel cliente: lista){
                if(cliente.getNombre_cliente().equals(busqueda)){
                    mostrarCliente(cliente);
                    contador++;
                }
            }
            if(contador == 0){
                Toast.makeText(this,"Cliente no encontrado",Toast.LENGTH_SHORT).show();
            }
        }
    }
}