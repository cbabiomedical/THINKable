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

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<Post> postList;
    Context context;

    public PostAdapter() {
    }

    public PostAdapter(List<Post> postList, Context context) {
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
        Post post=postList.get(position);
        holder.name.setText("Name= "+String.valueOf(post.getName()));
        holder.age.setText("Age= "+String.valueOf(post.getAge()));
        holder.school.setText("School= "+String.valueOf(post.getSchool()));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView age;
        TextView school;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            school=itemView.findViewById(R.id.school);



        }
    }

}
