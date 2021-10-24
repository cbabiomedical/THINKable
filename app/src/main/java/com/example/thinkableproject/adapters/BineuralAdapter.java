package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.BineuralAcivity;
import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.BineuralModelClass;
import com.example.thinkableproject.sample.GameModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class BineuralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BineuralModelClass> bineuralList;
    private Context context;

    public BineuralAdapter(List<BineuralModelClass> bineuralList) {
        this.bineuralList = bineuralList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grid_bineural, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).mName.setText(bineuralList.get(i).getBineuralName());
        ((ViewHolder) viewHolder).isFav.setImageResource(bineuralList.get(i).getIsFav());
//
        ((ViewHolder) viewHolder).mImage.setImageResource(bineuralList.get(i).getBineuralImage());
    }

    @Override
    public int getItemCount() {
        return bineuralList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mName;
        ImageView isFav;
        boolean isFavourite = false;
        DatabaseReference reference;
        FirebaseUser mUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.gridImage);
            mName = itemView.findViewById(R.id.item_name);
            isFav = itemView.findViewById(R.id.favouritesIcon);
            mUser = FirebaseAuth.getInstance().getCurrentUser();


        }

    }

}
