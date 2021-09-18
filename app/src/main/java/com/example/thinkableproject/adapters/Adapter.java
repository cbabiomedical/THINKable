package com.example.thinkableproject.adapters;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.ItemTouchHelperAdapter;
import com.example.thinkableproject.sample.ModelClass;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<ModelClass> userList;
    private ItemTouchHelper mTouchHelper;
    int positionChanged;

    public Adapter(List<ModelClass> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            int resource=userList.get(position).getImageView();
            String name=userList.get(position).getName();
            holder.setData(resource,name);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPositions) {
        ModelClass fromNote=userList.get(fromPosition);
        userList.remove(fromPosition);
        userList.add(toPositions,fromNote);
        Log.d("FROM", String.valueOf(fromNote));
        Log.d("TO", String.valueOf(toPositions));
        positionChanged=toPositions+1;
        setGetPositionChangedElement(toPositions+1);
        getPositionChanged();
        Log.d("POS", String.valueOf(toPositions+1));
        notifyItemMoved(fromPosition,toPositions);
    }

    private int getPositionChanged() {
        return positionChanged;
    }

    private void setGetPositionChangedElement(int i) {
        this.positionChanged=i;
    }

    @Override
    public void onItemSwipped(int position) {

    }
    public void setmTouchHelper(ItemTouchHelper itemTouchHelper){
        this.mTouchHelper=itemTouchHelper;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private CircleImageView imageView;
        private TextView namePref;
        GestureDetector mGestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            namePref=itemView.findViewById(R.id.image_name);

            mGestureDetector= new GestureDetector(itemView.getContext(),this);
            itemView.setOnTouchListener(this);

        }

        public void setData(int resource, String name) {
            imageView.setImageResource(resource);
            namePref.setText(name);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }
}
