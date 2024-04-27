package com.gestionfacturas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.gestionfacturas.adapters.TabAdapter;
import com.gestionfacturas.models.EmpleadoModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class InformesActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private EmpleadoModel empleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        tabLayout = findViewById(R.id.contenedortabs);
        viewPager = findViewById(R.id.vp_tabs);
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("EMPLEADO");
        TabAdapter tabAdapter = new TabAdapter(this,empleado.getId_empresa());
        viewPager.setAdapter(tabAdapter);
        // Creamos los tabs
        if(empleado.getTipo_empleado().equals(EmpleadoModel.ROL_ADMINISTRADOR)){
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.clientes);
                                tab.setIcon(R.drawable.clienteicon);
                                break;
                            case 1:
                                tab.setText(R.string.empleados);
                                tab.setIcon(R.drawable.empleadoicon);
                                break;
                            case 2:
                                tab.setText(R.string.fecha);
                                tab.setIcon(R.drawable.calendario);
                                break;
                            default:
                                break;
                        }
                    }
            ).attach();
        }else{
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.clientes);
                                tab.setIcon(R.drawable.clienteicon);
                                break;
                            default:
                                break;
                        }
                    }
            ).attach();
        }

    }



}