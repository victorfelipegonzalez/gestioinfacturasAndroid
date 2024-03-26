package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gestionfacturas.R;
import com.gestionfacturas.VisualizarPDFActivity;
import com.gestionfacturas.api.APIConnection;
import com.gestionfacturas.api.ApiService;
import com.gestionfacturas.models.ResponseModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FechaFragment extends Fragment {
    private View view;
    private Long id_empresa;
    private ArrayList<Integer> listaAnios;
    private Gson gson = new Gson();
    private Spinner spAnios;
    private Button volver,mostrarInformeAnual,mostrarInformeAnualClientes;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public FechaFragment(long id_empresa) {
        this.id_empresa = id_empresa;
        listaAnios = new ArrayList<>();
    }
    // Puedes añadir código específico de ClientesFragment aquí si es necesario
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        view = inflater.inflate(R.layout.fragmento_fecha, container, false);
        spAnios = view.findViewById(R.id.sp_anios);
        mostrarInformeAnualClientes = view.findViewById(R.id.bt_mostrar_informe_anual_clientes);
        mostrarInformeAnualClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarInformeAnualClientes();
            }
        });
        mostrarInformeAnual = view.findViewById(R.id.bt_mostrar_informe_anual);
        mostrarInformeAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarInformeAnual();
            }
        });
        volver = view.findViewById(R.id.bt_volver_informes_fecha);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverFecha();
            }
        });
        obtenerListaAnios();

        return view;
    }
    // Método para volver a la activity anterior
    public void volverFecha(){
        getActivity().finish();
    }

    // Método para mostrar el informe anual de clientes
    public void mostrarInformeAnualClientes(){
        int anio = (int) spAnios.getSelectedItem();
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel>call = apiService.generarInformeAnualClientes(id_empresa,anio);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess()==0){
                        // Decodificar el contenido de la respuesta de Base64 a byte[]
                        String contenido = (String) responseModel.getData();
                        byte[] pdfBytes = Base64.decode(contenido, Base64.DEFAULT);

                        try {
                            // Guardar el archivo PDF en la memoria del dispositivo
                            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "informeanualclientes.pdf");
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
                        Toast.makeText(view.getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(), "No hay datos del año seleccionado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para mostrar el informe anual
    public void mostrarInformeAnual(){
        int anio = (int) spAnios.getSelectedItem();
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel>call = apiService.generarInformeAnual(id_empresa,anio);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess()==0){
                        // Decodificar el contenido de la respuesta de Base64 a byte[]
                        String contenido = (String) responseModel.getData();
                        byte[] pdfBytes = Base64.decode(contenido, Base64.DEFAULT);

                        try {
                            // Guardar el archivo PDF en la memoria del dispositivo
                            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "informeanual.pdf");
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
                        Toast.makeText(view.getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(), "No hay datos del año seleccionado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener la lista de años en los que hay facturas
    public void obtenerListaAnios(){
        ApiService apiService = APIConnection.getApiService();
        Call<ResponseModel> call = apiService.obtenerListaAniosFacturas(id_empresa);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    ResponseModel responseModel = response.body();
                    if(responseModel.getSuccess()==0){
                        String stringAnios= gson.toJson(responseModel.getData());
                        Integer[] anios = gson.fromJson(stringAnios, Integer[].class);
                        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,anios);
                        spAnios.setAdapter(adapter);
                    }else{
                        Toast.makeText(view.getContext(), "Error al mostrar los años de facturación", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(), "No hay datos del año seleccionado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
