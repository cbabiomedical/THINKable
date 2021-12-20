package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EEG_Values;
import com.example.thinkableproject.R;

import java.util.List;

public class EEGAdapter extends RecyclerView.Adapter<EEGAdapter.ViewHolder> {
    List<EEG_Values> eeg_values;
    Context context;

    public EEGAdapter(List<EEG_Values> eeg_values, Context context) {
        this.eeg_values = eeg_values;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_eeg_val, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EEG_Values eeg=eeg_values.get(position);
        holder.alpha.setText(String.valueOf( "Alpha= "+eeg.getAlpha()));
        holder.beta.setText(String.valueOf("Beta= "+eeg.getBeta()));
        holder.gamma.setText(String.valueOf("Gamma= "+eeg.getGamma()));
        holder.theta.setText( String.valueOf("Theta= "+eeg.getTheta()));
        holder.delta.setText(String.valueOf("Delta= "+eeg.getDelta()));


    }

    @Override
    public int getItemCount() {
        return eeg_values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView alpha;
        TextView beta;
        TextView gamma;
        TextView theta;
        TextView delta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alpha=itemView.findViewById(R.id.alpha);
            beta=itemView.findViewById(R.id.beta);
            gamma=itemView.findViewById(R.id.gamma);
            theta=itemView.findViewById(R.id.theta);
            delta=itemView.findViewById(R.id.delta);
        }
    }
}
