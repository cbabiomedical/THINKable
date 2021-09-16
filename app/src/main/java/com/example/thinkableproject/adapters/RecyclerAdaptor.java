package com.example.thinkableproject.adapters;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.ItemTouchHelperAdapter;
import com.example.thinkableproject.sample.UserPreferences;


import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private List<UserPreferences> mNicePlaces = new ArrayList<>();
    private Context mContext;
    private ItemTouchHelper mTouchHelper;
    private int positionChanged;

    public RecyclerAdaptor() {


    }

    public int getPositionChanged() {
        Log.d("GET", String.valueOf(positionChanged));
        return positionChanged;
    }

    //    public int getPositionChangedElement(){
//
//        return positionChanged;
//    }
    public void setGetPositionChangedElement(int position){
        positionChanged=position;
        Log.d("SET", String.valueOf(positionChanged));
    }
    public RecyclerAdaptor(Context context, List<UserPreferences> userPreferences) {
        mNicePlaces = userPreferences;
        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_listitem, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        // Set the name of the 'NicePlace'
        ((ViewHolder)viewHolder).mName.setText(mNicePlaces.get(i).getPreferenceName());
        ((ViewHolder)viewHolder).mNum.setText(String.valueOf(mNicePlaces.get(i).getPreferenceNum()));

        // Set the image
        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_launcher_background);
        Glide.with(mContext)
                .setDefaultRequestOptions(defaultOptions)
                .load(mNicePlaces.get(i).getImageUrl())
                .into(((ViewHolder)viewHolder).mImage);
    }

    @Override
    public int getItemCount() {
        return mNicePlaces.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPositions) {
        UserPreferences fromNote=mNicePlaces.get(fromPosition);
        mNicePlaces.remove(fromPosition);
        mNicePlaces.add(toPositions,fromNote);
        Log.d("FROM", String.valueOf(fromNote));
        Log.d("TO", String.valueOf(toPositions));
        positionChanged=toPositions+1;
        setGetPositionChangedElement(toPositions+1);
        getPositionChanged();
        Log.d("POS", String.valueOf(toPositions+1));
        notifyItemMoved(fromPosition,toPositions);
    }

    @Override
    public void onItemSwipped(int position) {

    }
    public void setmTouchHelper(ItemTouchHelper itemTouchHelper){
        this.mTouchHelper=itemTouchHelper;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {

        private CircleImageView mImage;
        private TextView mName;
        TextView mNum;
        GestureDetector mGestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mName = itemView.findViewById(R.id.image_name);
            mNum=itemView.findViewById(R.id.num);

            mGestureDetector= new GestureDetector(itemView.getContext(),this);
            itemView.setOnTouchListener(this);

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
