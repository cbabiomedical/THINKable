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
import com.example.thinkableproject.sample.Post;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<EEG_Values> postList;
    Context context;

    public PostAdapter() {
    }

    public PostAdapter(List<EEG_Values> postList, Context context) {
        this.postList=postList;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EEG_Values post=postList.get(position);
        holder.alpha.setText(post.getAlpha());
        holder.beta.setText(post.getBeta());
        holder.gamma.setText(post.getGamma());
        holder.theta.setText(post.getTheta());
        holder.delta.setText(post.getTheta());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
