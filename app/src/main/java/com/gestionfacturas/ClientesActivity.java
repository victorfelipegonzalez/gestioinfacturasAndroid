package com.gestionfacturas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private List<ClienteModel> lista;
    private List<ClienteModel> filteredLista;
    private Button volver;
    private AutoCompleteTextView busquedaClientes;
    private ListView listView;
    private Gson gson = new Gson();
    private long id_empresa;
    private AlertDialog.Builder builder;
    private EmpleadoModel empleado;
    private ArrayAdapter<ClienteModel> adapter; // Adapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        volver = findViewById(R.id.bt_volver_crear_factura);
        listView = findViewById(R.id.list_listaClientes);
        busquedaClientes = findViewById(R.id.et_nombreClienteFactura);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        id_empresa = getIntent().getLongExtra("IDEMPRESA", 0);
        obtenerListaClientes();

        busquedaClientes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarCliente(filteredLista.get(position));
            }
        });
    }

    private void filter(String text) {
        filteredLista.clear();
        if (text.isEmpty()) {
            filteredLista.addAll(lista);
        } else {
            for (ClienteModel cliente : lista) {
                if (cliente.getNombre_cliente().toLowerCase().contains(text.toLowerCase())) {
                    filteredLista.add(cliente);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void obtenerListaClientes() {
        try {
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
                            String stringClientes = gson.toJson(responseModel.getData());
                            ClienteModel[] clienteArray = gson.fromJson(stringClientes, ClienteModel[].class);
                            // Convertir el array a una lista
                            lista = new ArrayList<>(Arrays.asList(clienteArray));
                            filteredLista = new ArrayList<>(lista);
                            adapter = new ArrayAdapter<>(ClientesActivity.this, android.R.layout.simple_list_item_1, filteredLista);
                            listView.setAdapter(adapter);
                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("ERROR", "Error en la respuesta: " + errorBody);
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
                    Toast.makeText(ClientesActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void mostrarCliente(ClienteModel cliente) {
        String mensaje = "  Nombre: " + cliente.getNombre_cliente() +
                "\n  DNI: " + cliente.getNif_cliente() +
                "\n  Teléfono: " + cliente.getTelefono_cliente() +
                "\n  Correo: " + cliente.getCorreo_cliente() +
                "\n  Dirección: " + cliente.getDireccion_cliente() +
                "\n  Ciudad: " + cliente.getCiudad_cliente() +
                "\n  CP: " + cliente.getCp_cliente() +
                "\n  Pais: " + cliente.getPais_cliente();

        // Se crea un TextView para mostrar el mensaje
        TextView textView = new TextView(this);
        textView.setText(mensaje);
        textView.setTextSize(16);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setPadding(10, 10, 10, 10);

        // Reconocer automáticamente teléfonos y correos electrónicos como enlaces
        Linkify.addLinks(textView, Linkify.PHONE_NUMBERS | Linkify.EMAIL_ADDRESSES);
        // Generar un AlerDialog para mostrar los datos

        builder = new AlertDialog.Builder(this);
        builder.setTitle("CLIENTE")
                .setIcon(R.drawable.clientes)
                .setView(textView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cierra el diálogo
                        dialog.dismiss();
                    }
                })/*.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Mostrar mensaje de confirmación para borrar el cliente
                        confirmarBorradoCliente(cliente);
                        dialog.dismiss();
                    }
                })*/.setNeutralButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Abrir la actividad o fragmento de edición de cliente
                        // Pasa los detalles del cliente actual como argumentos
                        actualizarCliente(cliente);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void volverListaClientes(View v) {
        finish();
    }

    private void confirmarBorradoCliente(ClienteModel cliente) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle("Confirmar borrado")
                .setMessage("¿Estás seguro de que deseas borrar este cliente?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar el cliente de la lista y actualizar la vista
                        borrarCliente(cliente.getId_cliente());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void borrarCliente(long id) {
        // Establecer la conexión
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel> call = apiService.borrarCliente(id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    ResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getSuccess() == 0) {
                        Toast.makeText(ClientesActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        obtenerListaClientes();
                    } else {
                        // Manejar la respuesta exitosa pero sin datos o con un código de error
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("ERROR", "Error en la respuesta: " + errorBody);
                            Toast.makeText(ClientesActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(ClientesActivity.this, "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("API Call", "Error en la llamada API", t);
                Toast.makeText(ClientesActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarCliente(ClienteModel clienteModel) {
        Intent intent = new Intent(this, RegistroClienteActivity.class);
        intent.putExtra("CLIENTE", clienteModel);
        intent.putExtra("EMPLEADO", empleado);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                obtenerListaClientes();
                busquedaClientes.setText(null);
            }
        }
    }
}