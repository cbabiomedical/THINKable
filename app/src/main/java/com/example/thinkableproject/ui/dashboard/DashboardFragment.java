package com.example.thinkableproject.ui.dashboard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.thinkableproject.R;
import com.example.thinkableproject.adapters.FavouriteMeditationAdapter;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.sample.FavouriteModelMeditationClass;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavMeditationDB favDB;
    private List<FavouriteModelMeditationClass> favItemList = new ArrayList<>();
    private FavouriteMeditationAdapter favAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        favDB = new FavMeditationDB(getActivity());
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // add item touch helper

        loadData();

        return root;
    }

    private void loadData() {
        if (favItemList != null) {
            favItemList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list_med();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(FavMeditationDB.ITEM_TITLEMED));
                String id = cursor.getString(cursor.getColumnIndex(FavMeditationDB.KEY_IDMED));
                String image = cursor.getString(cursor.getColumnIndex(FavMeditationDB.ITEM_IMAGEMED));
                FavouriteModelMeditationClass favItem = new FavouriteModelMeditationClass(title, id, image);
                favItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        favAdapter = new FavouriteMeditationAdapter( getActivity(),favItemList);

        recyclerView.setAdapter(favAdapter);

    }
}