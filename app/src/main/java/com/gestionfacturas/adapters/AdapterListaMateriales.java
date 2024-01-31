package com.gestionfacturas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gestionfacturas.R;
import com.gestionfacturas.models.LineaFacturaModel;
import com.gestionfacturas.models.ProductoModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class AdapterListaMateriales extends RecyclerView.Adapter<AdapterListaMateriales.ViewHolder>{
    private ArrayList<ProductoModel> listaItems;
    private ArrayList<LineaFacturaModel> listaLineas;
    private OnItemClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDescripcion;
        public ImageButton ibBuscarProducto;


        public ViewHolder(View view) {
            super(view);
            textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
            ibBuscarProducto = view.findViewById(R.id.ib_buscar_producto);
        }
    }

    public AdapterListaMateriales(ArrayList<ProductoModel> listaItems,ArrayList<LineaFacturaModel> lineasFacturas, OnItemClickListener clickListener) {
        this.listaItems = listaItems;
        this.listaLineas = lineasFacturas;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linear_layout_model, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LineaFacturaModel linea = listaLineas.get(position);
        ProductoModel producto = new ProductoModel();
        for (ProductoModel productoModel : listaItems){
            if(productoModel.getId_producto() == linea.getId_producto()){
                producto = productoModel;
            }
        }

        holder.textViewDescripcion.setText(producto.getDescripcion()+": "+linea.getCantidad());

        holder.ibBuscarProducto.setOnClickListener(v -> {
            // Implementar la lógica para eliminar el elemento
            // y notifica al adaptador sobre el cambio.
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listaLineas.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaLineas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
