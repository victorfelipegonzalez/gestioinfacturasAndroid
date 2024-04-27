package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gestionfacturas.adapters.AdapterPersonalizado;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.ProductoModel;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EmpleadoModel empleado;
    private String[]titulos;
    private int[]images;
    private GridView panel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        panel = findViewById(R.id.gv_panel);
        modoVisualizacion();


    }
    // Método para gestionar los datos recibidos y
    // establecer la lista de opciones en relación al tipo de empleado
    public void modoVisualizacion(){
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        if (empleado.getTipo_empleado().equals(EmpleadoModel.ROL_ADMINISTRADOR)){
            titulos= new String[]{getResources().getString(R.string.anadir_nclliente),
                    getResources().getString(R.string.informaci_n_de_clientes),
                    getResources().getString(R.string.generar_factura),
                    getResources().getString(R.string.informes_de_nfacturacion),
                    getResources().getString(R.string.anadir_empleado_main),
                    getResources().getString(R.string.anadir_nproducto)
                    };
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
                    getResources().getString(R.string.generar_factura),
                    getResources().getString(R.string.informes_de_nfacturacion)};
            images = new int[]{R.drawable.anadircliente,
                    R.drawable.clientes,
                    R.drawable.iconoaplicacion,
                    R.drawable.informes};
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
    //Método para ir a una activity dependiendo de la posicion pulsada
    public void irA(int position){
        if (position == 0) {
            anadirCliente();
        } else if (position == 1) {
            listaClientes();
        } else if (position == 2) {
            facturas();
        }else if(position == 3){
            informes();
        }else if (position == 4) {
            anadirEmpleado();
        }else if (position == 5) {
            productos();
        }
    }
    //Método para inflar el menú
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return true;
    }
    // Método para comprobar la opción del menú que se ha selecionado
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.salir) {
           logout();
        }
        return true;
    }
    // Método para ir a lista de clientes
    public void listaClientes(){
        Intent intent =new Intent(this, ClientesActivity.class);
        intent.putExtra("EMPLEADO",empleado);
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
    }
}