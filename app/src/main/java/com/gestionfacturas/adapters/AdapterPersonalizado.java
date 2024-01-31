package com.gestionfacturas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gestionfacturas.R;

public class AdapterPersonalizado extends BaseAdapter {
    private Context context;
    private int images[];
    private String [] titles;
    private LayoutInflater inflater;

    public AdapterPersonalizado(Context applicationContext, int[]images, String[] titles){
        this.context = applicationContext;
        this.images = images;
        this.inflater = LayoutInflater.from(applicationContext);
        this.titles = titles;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.contenedor_model,null);
        ImageView image = convertView.findViewById(R.id.image);
        TextView tv = convertView.findViewById(R.id.tv_titulo);
        image.setImageResource(images[position]);
        tv.setText(titles[position]);
        return convertView;
    }
}
