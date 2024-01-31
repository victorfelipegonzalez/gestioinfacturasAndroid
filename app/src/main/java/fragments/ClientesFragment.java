package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gestionfacturas.R;
import com.gestionfacturas.VisualizarPDFActivity;
import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ClienteModel;
import com.gestionfacturas.models.FacturaModel;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesFragment extends Fragment {
    private AutoCompleteTextView nombreClientes;
    private ImageButton buscarClientes;
    private ListView listaFacturasClientes;
    private View view;
    private Long id_empresa;
    private Gson gson = new Gson();
    private List<ClienteModel> lista;
    private ArrayList<String> nombreClientesLista;
    private ArrayList<FacturaModel> listaFacturas;
    private Button volver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public ClientesFragment(Long idEmpresa) {
        this.id_empresa = idEmpresa;
        lista = new ArrayList<>();
        nombreClientesLista = new ArrayList<>();
    }
    // Método para crear la vista de la Tab
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        view = inflater.inflate(R.layout.fragmento_clientes,container,false);
        volver = view.findViewById(R.id.bt_volver_informes_clientes);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverInformesClientes();
            }
        });
        nombreClientes = view.findViewById(R.id.et_nombreClienteInforme);
        buscarClientes = view.findViewById(R.id.ib_buscar_cliente_informe);
        buscarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarCliente();
            }
        });
        listaFacturasClientes = view.findViewById(R.id.lv_listafacturasclientes);
        listaFacturasClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generarInformeFactura(listaFacturas.get(position).getId_factura());
            }
        });
        obtenerListaClientes();
        return view;
    }

    // Método para rellenar la lista de clientes del AutoCompleTextView
    public void obtenerListaClientes(){
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
                            for(ClienteModel cliente: datos) {
                                nombreClientesLista.add(cliente.getNombre_cliente());
                                lista.add(cliente);
                            }
                            ArrayAdapter<String> nombres = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line,nombreClientesLista);
                            nombreClientes.setAdapter(nombres);
                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("ERROR","Error en la respuesta: "+ errorBody);
                                Toast.makeText(view.getContext(), "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("API Call", "Error en la llamada API", t);
                    Toast.makeText(view.getContext(), "Error en la llamada API: ", Toast.LENGTH_SHORT).show();
                }
            });
    }

    // Método para obtener la lista de facturas del cliente selecionado
    public void obtenerListaFacturas(Long id_cliente){
        listaFacturas = new ArrayList<>();
        ApiService apiService = APIConnection.getApiService();
        Call <ResponseModel> call = apiService.obtenerListaFacturasCliente(id_empresa, id_cliente);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess() == 0){
                        String stringFacturas= gson.toJson(responseModel.getData());
                        FacturaModel[] facturaArray = gson.fromJson(stringFacturas, FacturaModel[].class);
                        List<FacturaModel> datos = Arrays.asList(facturaArray);

                        for(FacturaModel factura: datos) {
                            listaFacturas.add(factura);
                        }
                        ArrayAdapter adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,listaFacturas);
                        listaFacturasClientes.setAdapter(adapter);
                    }else{
                        // Manejar la respuesta exitosa pero sin datos o con un código de error
                            Toast.makeText(view.getContext(), "Clientes sin facturas asociadas", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });

    }

    public void comprobarCliente(){
        String busqueda = String.valueOf(nombreClientes.getText());
        int contador = 0;
        if(busqueda.isEmpty()){
            Toast.makeText(view.getContext(),"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
        }else{
            for(ClienteModel cliente: lista){
                if(cliente.getNombre_cliente().equals(busqueda)){
                    obtenerListaFacturas(cliente.getId_cliente());
                    contador++;
                }
            }
            if(contador == 0){
                Toast.makeText(view.getContext(),"Facturas de cliente no encontradas",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void volverInformesClientes(){
        getActivity().finish();
    }

    // Método para generar un PDF de la factura seleccionada
    public void generarInformeFactura(long id_factura){
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel> call = apiService.generarInformeFactura(id_factura);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess() == 0){
                        // Decodificar el contenido de la respuesta de Base64 a byte[]
                        String contenido = (String) responseModel.getData();
                        byte[] pdfBytes = Base64.decode(contenido, Base64.DEFAULT);

                        try {
                            // Guardar el archivo PDF en la memoria del dispositivo
                            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "factura.pdf");
                            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                            fileOutputStream.write(pdfBytes);
                            fileOutputStream.close();

                            // Abrir el archivo PDF en aplicacion predeterminada
                            Intent intent = new Intent(view.getContext(), VisualizarPDFActivity.class);
                            intent.putExtra("PDF",pdfFile);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // Manejar la respuesta exitosa pero sin datos
                            Toast.makeText(view.getContext(), "Cliente sin facturas", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
