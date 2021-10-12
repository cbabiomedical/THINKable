package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class GridAdapter extends BaseAdapter {
    Context contexts;
    String[] gameName;
    int[] images;
    boolean isFav;


    public GridAdapter(Context contexts, String[] gameName, int[] images, boolean isFav) {
        this.contexts = contexts;
        this.gameName = gameName;
        this.images = images;
        this.isFav = isFav;

    }

    LayoutInflater inflater;

    @Override
    public int getCount() {
        return gameName.length;
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
        if (inflater == null)
            inflater = (LayoutInflater) contexts.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
        }
        ImageView imageView = convertView.findViewById(R.id.gridImage);
        TextView textView = convertView.findViewById(R.id.item_name);
        imageView.setImageResource(images[position]);
        textView.setText(gameName[position]);
        ImageView favImageView = convertView.findViewById(R.id.favouritesIcon);
        return convertView;
    }
}
