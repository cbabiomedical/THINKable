package com.example.thinkableproject.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.thinkableproject.R;
import com.example.thinkableproject.adapters.RecyclerAdaptor;
import com.example.thinkableproject.sample.UserPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
//Singleton Pattern
public class UserPreferenceRepository {

    private static UserPreferenceRepository instance;
    private ArrayList<UserPreferences> dataSet = new ArrayList<>();
    DatabaseReference reference =FirebaseDatabase.getInstance().getReference().child("Users");


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


    private void setNicePlaces(){
        dataSet.add(
                new UserPreferences(R.drawable.video,
                        "Videos")
        );
        dataSet.add(
                new UserPreferences(R.drawable.music,
                        "Music")
        );
        dataSet.add(
                new UserPreferences(R.drawable.meditation,
                        "Meditation")
        );
        dataSet.add(
                new UserPreferences(R.drawable.controller,
                        "Games")
        );
        dataSet.add(
                new UserPreferences(R.drawable.binaural,
                        "Bineural Waves")
        );
        dataSet.add(
                new UserPreferences(R.drawable.playtime,
                        "Kids")
        );
        dataSet.add(
                new UserPreferences(R.drawable.book,
                        "Book")
        );

    }
}
