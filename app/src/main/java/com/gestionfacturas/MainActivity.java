package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gestionfacturas.adapters.AdapterPersonalizado;
import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.EmpresaModel;
import com.gestionfacturas.models.ProductoModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EmpleadoModel empleado;
    private String[]titulos;
    private int[]images;
    private GridView panel;
    private Gson gson;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        panel = findViewById(R.id.gv_panel);
        gson = new Gson();
        modoVisualizacion();


    }

    public void modoVisualizacion(){
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        if (empleado.getTipo_empleado().equals(EmpleadoModel.ROL_ADMINISTRADOR)){
            titulos= new String[]{getResources().getString(R.string.anadir_nclliente),
                    getResources().getString(R.string.informaci_n_de_clientes),
                    getResources().getString(R.string.generar_factura),
                    getResources().getString(R.string.anadir_nproducto),
                    getResources().getString(R.string.anadir_empleado_main),
                    getResources().getString(R.string.informes_de_nfacturacion)};
            images= new int[]{R.drawable.anadircliente,
                    R.drawable.clientes,
                    R.drawable.iconoaplicacion,
                    R.drawable.anadirproducto,
                    R.drawable.empleado,
                    R.drawable.informes};
            AdapterPersonalizado adapter = new AdapterPersonalizado(this,images,titulos);
            panel.setAdapter(adapter);
        }else {
            titulos = new String[]{getResources().getString(R.string.anadir_nclliente),
                    getResources().getString(R.string.informaci_n_de_clientes),
                    getResources().getString(R.string.generar_factura)};
            images = new int[]{R.drawable.anadircliente,
                    R.drawable.clientes,
                    R.drawable.iconoaplicacion};
            AdapterPersonalizado adapter = new AdapterPersonalizado(this, images, titulos);
            panel.setAdapter(adapter);
        }
        panel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                irA(position);
            }
        });
    }
    public void irA(int position){
        if (position == 0) {
            anadirCliente();
        } else if (position == 1) {
            listaClientes();
        } else if (position == 2) {
            facturas();
        }else if(position == 3){
            productos();
        }else if (position == 4) {
            anadirEmpleado();
        }else if (position == 5) {
            informes();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cambiar_contrasena) {

        } else if (item.getItemId() == R.id.salir) {
           logout();
        }
        return true;
    }
    // Método para ir a lista de clientes
    public void listaClientes(){
        Intent intent =new Intent(this, ClientesActivity.class);
        intent.putExtra("IDEMPRESA",empleado.getId_empresa());
        startActivity(intent);
    }
    //Método para ir a Activity para añadir cliente
    public void anadirCliente(){
        ClienteModel cliente = new ClienteModel();
        cliente.setId_empresa(empleado.getId_empresa());
        Intent intent = new Intent(this, RegistroClienteActivity.class);
        intent.putExtra("CLIENTE",cliente);
        intent.putExtra("EMPLEADO",empleado);
        startActivity(intent);
    }
    // Método para ir a Activity para añadir empleado
    public void anadirEmpleado(){
        Intent intent = new Intent(this,AnadirEmpleadoActivity.class);
        intent.putExtra("EMPLEADO",empleado);
        startActivity(intent);
    }
    // Método para ir a la Activity para añadir producto
    public void productos(){
        ProductoModel producto = new ProductoModel();
        producto.setId_empresa(empleado.getId_empresa());
        Intent intent = new Intent(this, ProductosActivity.class);
        intent.putExtra("PRODUCTO",producto);
        startActivity(intent);
    }
    // Método para ir a la Activity para crear facturas
    public void facturas(){
        Intent intent = new Intent(this,CrearFacturaActivity.class);
        intent.putExtra("EMPLEADO",empleado);
        startActivity(intent);
    }

    // Método para ir a la Activity para visualizar informes
    public void informes(){
        Intent intent = new Intent(this,InformesActivity.class);
        intent.putExtra("EMPLEADO",empleado);
        startActivity(intent);
    }
    // Método para salir e ir a la Activity Login
    public void logout() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }
}