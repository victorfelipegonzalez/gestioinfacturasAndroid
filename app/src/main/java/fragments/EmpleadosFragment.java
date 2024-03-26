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
import com.gestionfacturas.models.EmpleadoModel;
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

public class EmpleadosFragment extends Fragment {
    private Long id_empresa;
    private ArrayList<EmpleadoModel> lista;
    private Button volverInformesEmpleados, informeEstadisticas;
    private Gson gson = new Gson();
    private View view;
    private AutoCompleteTextView nombreEmpleados;
    private ArrayList<String> nombreEmpleadosLista;
    private ImageButton buscarEmpleados;
    private ArrayList<FacturaModel> listaFacturas;
    private ListView listaFacturasEmpleados;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public EmpleadosFragment(Long idEmpresa) {
        this.id_empresa = idEmpresa;
        lista = new ArrayList<>();
        listaFacturas = new ArrayList<>();
        nombreEmpleadosLista = new ArrayList<>();
    }
    // Puedes añadir código específico de EmpleadosFragment aquí si es necesario
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        view = inflater.inflate(R.layout.fragmento_empleados, container, false);

        informeEstadisticas = view.findViewById(R.id.bt_informe_estadisticas);
        listaFacturasEmpleados = view.findViewById(R.id.lv_lista_facturas_empleados);
        nombreEmpleados = view.findViewById(R.id.et_nombreEmpleadosInforme);
        buscarEmpleados = view.findViewById(R.id.ib_buscar_empleado_informe);
        buscarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarEmpleado();
            }
        });
        volverInformesEmpleados = view.findViewById(R.id.bt_volver_informes_empleados);
        volverInformesEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverInformesEmpleados();            }
        });
        listaFacturasEmpleados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generarInformeFactura(listaFacturas.get(position).getId_factura());
            }
        });
        informeEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarInformeEstadisticasEmpleados();
            }
        });
        obtenerListaEmpleados();

        return view;

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

                            // Abrir el archivo PDF en la activity de visualización de PDF
                            Intent intent = new Intent(view.getContext(), VisualizarPDFActivity.class);
                            intent.putExtra("PDF",pdfFile);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // Manejar la respuesta exitosa pero sin datos o con un código de error
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("ERROR","Error en la respuesta: "+ errorBody);
                            Toast.makeText(view.getContext(), "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener la lista de facturas asociadas al empleado seleccionado
    public void obtenerListaFacturasEmpleado(long id_empleado){
        listaFacturas = new ArrayList<>();
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel> call = apiService.obtenerListaFacturasEmpleado(id_empresa,id_empleado);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel != null && responseModel.getSuccess() == 0){
                        String stringFacturas= gson.toJson(responseModel.getData());
                        FacturaModel[] facturaArray = gson.fromJson(stringFacturas, FacturaModel[].class);
                        List<FacturaModel> datos = Arrays.asList(facturaArray);

                        for(FacturaModel factura: datos) {
                            listaFacturas.add(factura);
                        }
                        ArrayAdapter adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,listaFacturas);
                        listaFacturasEmpleados.setAdapter(adapter);
                    }else{
                        // Manejar la respuesta exitosa pero sin datos
                            Toast.makeText(view.getContext(), "Empleado sin facturas asociadas", Toast.LENGTH_SHORT).show();

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

    // Método para obtener la lista de empleados
    public void obtenerListaEmpleados(){
            // Establecer la conexión
            ApiService apiService = APIConnection.getApiService();
            // Obtener lista de clientes del servidor
            Call<ResponseModel> call = apiService.obtenerEmpleados(id_empresa);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        ResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getSuccess() == 0) {
                            // Obtener los datos de la respuesta obtenida por el servidor
                            String stringEmpleados= gson.toJson(responseModel.getData());
                            EmpleadoModel[] empleadoArray = gson.fromJson(stringEmpleados, EmpleadoModel[].class);
                            // Convertir el array a una lista
                            List<EmpleadoModel> datos = Arrays.asList(empleadoArray);
                            for(EmpleadoModel empleado: datos) {
                                nombreEmpleadosLista.add(empleado.getNombre_empleado());
                                lista.add(empleado);
                            }
                            ArrayAdapter<String> nombres = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line,nombreEmpleadosLista);
                            nombreEmpleados.setAdapter(nombres);
                        } else {
                            // Manejar la respuesta exitosa pero sin datos o con un código de error
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("ERROR","Error en la respuesta: "+ errorBody);
                                Toast.makeText(view.getContext(), "No hay facturas de este empleado", Toast.LENGTH_SHORT).show();
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
    // Método para obtener la lista de facturas que ha realizado el empleado selecionado
    public void comprobarEmpleado(){
        String busqueda = String.valueOf(nombreEmpleados.getText());
        int contador = 0;
        if(busqueda.isEmpty()){
            Toast.makeText(view.getContext(),"Debe introducir un nombre",Toast.LENGTH_SHORT).show();
        }else{
            for(EmpleadoModel empleado: lista){
                if(empleado.getNombre_empleado().equals(busqueda)){
                    // Conseguir lista de facturas
                    obtenerListaFacturasEmpleado(empleado.getId_empleado());
                    contador++;
                }
            }
            if(contador == 0){
                Toast.makeText(view.getContext(),"Facturas de empleado no encontradas",Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Método para volver a la activity anterior
    public void volverInformesEmpleados(){
        getActivity().finish();
    }

    // Método para generar un informe de estadisticas de las facturas
    public void generarInformeEstadisticasEmpleados(){
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel> call = apiService.generarInformeEstadisticas(id_empresa);
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
                            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "informeestadisticas.pdf");
                            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                            fileOutputStream.write(pdfBytes);
                            fileOutputStream.close();

                            // Abrir el archivo PDF en la activity de visualización de PDF
                            Intent intent = new Intent(view.getContext(), VisualizarPDFActivity.class);
                            intent.putExtra("PDF",pdfFile);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // Manejar la respuesta exitosa pero sin datos o con un código de error
                        Toast.makeText(view.getContext(), "Servidor sin respuesta", Toast.LENGTH_SHORT).show();
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