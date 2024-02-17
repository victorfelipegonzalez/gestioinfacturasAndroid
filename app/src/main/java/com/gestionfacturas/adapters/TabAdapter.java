package com.gestionfacturas.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fragments.ClientesFragment;
import fragments.EmpleadosFragment;
import fragments.FechaFragment;

public class TabAdapter extends FragmentStateAdapter {
    private ClientesFragment clientes;
    private EmpleadosFragment empleados;
    private FechaFragment fecha;

    public TabAdapter(FragmentActivity fragmentActivity,Long idEmpresa) {
        super(fragmentActivity);

        clientes = new ClientesFragment(idEmpresa);
        empleados = new EmpleadosFragment(idEmpresa);
        fecha = new FechaFragment(idEmpresa);
    }
    @Override
    public Fragment createFragment(int position) {
        // Retorna el fragmento correspondiente a la posición de la pestaña
        switch (position) {
            case 0:
                return clientes;
            case 1:
                return empleados;
            case 2:
                return fecha;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        // Retorna el número total de pestañas
        return 3;
    }
}
