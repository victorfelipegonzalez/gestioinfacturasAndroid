package com.gestionfacturas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gestionfacturas.adapters.AdapterPersonalizado;
import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.EmpleadoModel;
import com.gestionfacturas.models.EmpresaModel;
import com.gestionfacturas.models.ProductoModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EmpleadoModel empleado;
    private EmpresaModel empresa;
    private String[]titulos;
    private AlertDialog.Builder builder;
    private int[]images;
    private GridView panel;
    private Toolbar toolbar;
    private Gson gson;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        gson = new Gson();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        panel = findViewById(R.id.gv_panel);
        modoVisualizacion();


    }
    // Método para gestionar los datos recibidos y
    // establecer la lista de opciones en relación al tipo de empleado
    public void modoVisualizacion(){
        obtenerEmpresa(empleado.getId_empresa());
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem empresaInfo = menu.findItem(R.id.info);
        if (empleado != null && EmpleadoModel.ROL_ADMINISTRADOR.equals(empleado.getTipo_empleado())) {
            empresaInfo.setVisible(true);
        } else {
            empresaInfo.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    // Método para comprobar la opción del menú que se ha selecionado
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.salir) {
           logout();
        } else if (item.getItemId() == R.id.info) {
            mostrarEmpresa();
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
        finish();
    }

    public void obtenerEmpresa(long id){
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel> call = apiService.obtenerEmpresa(id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess() == 0){
                        // Obtener los datos de la respuesta obtenida por el servidor
                        String stringEmpresa = gson.toJson(responseModel.getData());
                        EmpresaModel empresaDevuelta = gson.fromJson(stringEmpresa, EmpresaModel.class);
                        // Establecemos la empresa que hemos recibido en nuestra variable
                        empresa = empresaDevuelta;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    private void mostrarEmpresa() {
        String mensaje = "  Nombre: "+empresa.getNombreEmpresa()+
                "\n  NIF: "+empresa.getNif()+
                "\n  Teléfono: "+empresa.getTelefono()+
                "\n  Correo: "+empresa.getCorreo()+
                "\n  Dirección: "+empresa.getDireccion()+
                "\n  Ciudad: "+empresa.getCiudad()+
                "\n  CP: "+empresa.getCp()+
                "\n  Pais: "+empresa.getPais();

        // Se crea un TextView para mostrar el mensaje
        TextView textView = new TextView(this);
        textView.setText(mensaje);
        textView.setTextSize(16);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setPadding(10, 10, 10, 10);

        // Generar un AlerDialog para mostrar los datos

        builder = new AlertDialog.Builder(this);
        builder.setTitle("DATOS EMPRESA")
                .setIcon(R.drawable.empresa)
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
                        // Abrir la actividad o fragmento de edición de empresa
                        // Pasa los detalles de la empresa actual como argumentos
                        actualizarDatosEmpresa();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void actualizarDatosEmpresa(){
        Intent intent = new Intent(this, ActualizarEmpresaActivity.class);
        intent.putExtra("EMPRESA",empresa);
        intent.putExtra("EMPLEADO",empleado);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
                empresa = (EmpresaModel) data.getSerializableExtra("EMPRESA");
            }
        }
    }
}