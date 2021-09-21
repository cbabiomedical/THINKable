package com.example.thinkableproject.sample;


public interface ItemTouchHelperAdapter {
    // Interface  methods for Drag and drop functionality
    void onItemMove(int fromPosition,int toPositions);
    void onItemSwipped(int position);
}
