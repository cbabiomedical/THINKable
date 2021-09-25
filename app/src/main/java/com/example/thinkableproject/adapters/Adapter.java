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
    private static ItemTouchHelper mTouchHelper;
    int positionChanged;

    //Constructor
    public Adapter(List<ModelClass> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initializing ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        // returning layout resource
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        //storing the imageView and name of preferences inside variables
        int resource = userList.get(position).getImageView();
        String name = userList.get(position).getName();
        // Calling setData method
        holder.setData(resource, name);
    }

    @Override
    public int getItemCount() {
        // returning the size of list
        return userList.size();

    }

    @Override
    public void onItemMove(int fromPosition, int toPositions) {
        ModelClass fromNote = userList.get(fromPosition);
        // removing current position from userlist
        userList.remove(fromPosition);
        userList.add(toPositions, fromNote);
        // adding new position and from position
        Log.d("FROM", String.valueOf(fromNote));
        Log.d("TO", String.valueOf(toPositions));
        positionChanged = toPositions + 1;
        //Calling setter and getter method for positionChnaged
        setGetPositionChangedElement(toPositions + 1);
        getPositionChanged();
        Log.d("POS", String.valueOf(toPositions + 1));
        //Notifying user that item reflected is newly inserted
        notifyItemMoved(fromPosition, toPositions);
    }

    private int getPositionChanged() {
        return positionChanged;
    }

    private void setGetPositionChangedElement(int i) {
        this.positionChanged = i;
    }

    @Override
    public void onItemSwipped(int position) {

    }

    public void setmTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.mTouchHelper = itemTouchHelper;
    }

    // Creating ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private CircleImageView imageView;  // Variable to store imageUrl and name
        private TextView namePref;
        GestureDetector mGestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            namePref = itemView.findViewById(R.id.image_name);
            //Initializing gesture detector
            mGestureDetector = new GestureDetector(itemView.getContext(), this);
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
