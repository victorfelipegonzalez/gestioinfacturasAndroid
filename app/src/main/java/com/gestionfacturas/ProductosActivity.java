package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ProductoModel;
import com.gestionfacturas.models.ResponseModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosActivity extends AppCompatActivity {
    private Button volver,guardar;
    private ProductoModel producto;
    private EditText descripcion,precio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        volver = findViewById(R.id.bt_volverProductor);
        guardar = findViewById(R.id.bt_guardarProductos);
        descripcion = findViewById(R.id.et_descripcion_producto);
        precio = findViewById(R.id.et_precio_producto);
        producto = (ProductoModel) getIntent().getSerializableExtra("PRODUCTO");
    }

    public void volverProductos(View v){
        finish();
    }

    public void guardarProducto(View v){
        if(comprobarCampos()){
            guardarProducto();
        }
    }

    public boolean comprobarCampos(){
        if (descripcion.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debes introducir una\ndescripción del producto", Toast.LENGTH_SHORT).show();
            return false;
        } else if (precio.getText().toString().isEmpty()) {
            Toast.makeText(this,"Debes introducir un precio",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            producto.setDescripcion(String.valueOf(descripcion.getText()));
            producto.setPrecio(Double.valueOf(String.valueOf(precio.getText())));
            return true;
        }
    }
    public void reiniciarCampos(){
        descripcion.setText(null);
        precio.setText(null);
    }
    // Método para guardar producto en la base de datos PostgreSQL
    public void guardarProducto(){
        ApiService apiService = APIConnection.getApiService();
        Call call = apiService.insertarProducto(producto);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call <ResponseModel> callResponse, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess()==0){
                        reiniciarCampos();
                        descripcion.requestFocus();
                        Toast.makeText(ProductosActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ProductosActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("ERROR","Error en la respuesta: "+ errorBody);
                        Toast.makeText(ProductosActivity.this, "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("API Call", "Error en la llamada API", t);
                Toast.makeText(ProductosActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}