package com.example.thinkableproject.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.thinkableproject.R;
import com.example.thinkableproject.adapters.RecyclerAdaptor;
import com.example.thinkableproject.sample.UserPreferences;

import java.util.ArrayList;
import java.util.List;
//Singleton Pattern
public class UserPreferenceRepository {

    private static UserPreferenceRepository instance;
    private ArrayList<UserPreferences> dataSet = new ArrayList<>();

    public static UserPreferenceRepository getInstance(){
        if(instance == null){
            instance = new UserPreferenceRepository();
        }
        return instance;
    }


    // Pretend to get data from a webservice or online source
    public MutableLiveData<List<UserPreferences>> getNicePlaces(){
        setNicePlaces();
        MutableLiveData<List<UserPreferences>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }
    RecyclerAdaptor recyclerAdaptor=new RecyclerAdaptor();

    public RecyclerAdaptor getRecyclerAdaptor() {
        recyclerAdaptor.getPositionChanged();
        return recyclerAdaptor;

    }

    private void setNicePlaces(){
        dataSet.add(
                new UserPreferences(R.drawable.video,
                        "Videos",recyclerAdaptor.getPositionChanged())
        );
        dataSet.add(
                new UserPreferences(R.drawable.music,
                        "Music",recyclerAdaptor.getPositionChanged())
        );
        dataSet.add(
                new UserPreferences(R.drawable.meditation,
                        "Meditation",recyclerAdaptor.getPositionChanged())
        );
        dataSet.add(
                new UserPreferences(R.drawable.controller,
                        "Games",recyclerAdaptor.getPositionChanged())
        );
        dataSet.add(
                new UserPreferences(R.drawable.binaural,
                        "Bineural Waves",recyclerAdaptor.getPositionChanged())
        );
        dataSet.add(
                new UserPreferences(R.drawable.playtime,
                        "Kids",recyclerAdaptor.getPositionChanged())
        );
        dataSet.add(
                new UserPreferences(R.drawable.book,
                        "Book",recyclerAdaptor.getPositionChanged())
        );

    }
}
