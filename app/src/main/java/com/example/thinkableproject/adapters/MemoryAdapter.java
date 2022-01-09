package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.models.MemoryInterventionClass;

import java.util.ArrayList;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MemoryInterventionClass> memoryInterventionList;
    OnNoteListner onNoteListner;

    public MemoryAdapter(Context context, ArrayList<MemoryInterventionClass> memoryInterventionList, OnNoteListner onNoteListner) {
        this.context = context;
        this.memoryInterventionList = memoryInterventionList;
        this.onNoteListner=onNoteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_intervention_list, parent, false);
        return new ViewHolder(view,onNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.interventionBg.setImageResource(memoryInterventionList.get(position).getImageView());
        holder.title.setText(memoryInterventionList.get(position).getTitle());
        holder.text.setText(memoryInterventionList.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return memoryInterventionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView interventionBg;
        TextView title;
        TextView text;
       OnNoteListner onNoteListner;


        public ViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);
            interventionBg = itemView.findViewById(R.id.music);
            title = itemView.findViewById(R.id.musictxt);
            text = itemView.findViewById(R.id.musictxt2);
            this.onNoteListner = onNoteListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListner.onNoteClickMeditation(getAdapterPosition());

        }
    }

    public interface OnNoteListner {
        void onNoteClickMeditation(int position);
    }


}
