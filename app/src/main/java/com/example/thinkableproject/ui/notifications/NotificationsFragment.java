package com.example.thinkableproject.ui.notifications;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.adapters.FavouriteAdapter;
import com.example.thinkableproject.adapters.FavouriteMusicAdapter;
import com.example.thinkableproject.repositories.FavDB;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.FavouriteModelClass;
import com.example.thinkableproject.sample.FavouriteMusicClass;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavMusicDB favDB;
    private ArrayList<FavouriteMusicClass> favItemList = new ArrayList<>();
    private FavouriteMusicAdapter favAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        favDB = new FavMusicDB(getActivity());
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
        Cursor cursor = favDB.select_all_favorite_list_mus();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(FavMusicDB.ITEM_TITLEMUS));
                String id = cursor.getString(cursor.getColumnIndex(FavMusicDB.KEY_IDMUS));
                String image = cursor.getString(cursor.getColumnIndex(FavMusicDB.ITEM_IMAGEMUS));
                FavouriteMusicClass favItem = new FavouriteMusicClass(title, id, image);
                favItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        favAdapter = new FavouriteMusicAdapter( getActivity(),favItemList);

        recyclerView.setAdapter(favAdapter);

    }




}