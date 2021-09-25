package com.example.thinkableproject.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.thinkableproject.repositories.UserPreferenceRepository;
import com.example.thinkableproject.sample.UserPreferences;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<UserPreferences>> mNicePlaces;
    private UserPreferenceRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init() {
        if (mNicePlaces != null) {
            return;
        }
        mRepo = UserPreferenceRepository.getInstance();
        mNicePlaces = mRepo.getNicePlaces();

    }

    public void addNewValue(final UserPreferences nicePlace) {
        mIsUpdating.setValue(true);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                List<UserPreferences> currentPlaces = mNicePlaces.getValue();
                currentPlaces.add(nicePlace);
                mNicePlaces.postValue(currentPlaces);
                mIsUpdating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public LiveData<List<UserPreferences>> getNicePlaces() {
        return mNicePlaces;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}
