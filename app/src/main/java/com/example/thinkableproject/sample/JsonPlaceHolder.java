package com.example.thinkableproject.sample;

import com.example.EEG_Values;

import org.w3c.dom.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolder {
    @GET("eeg_values")
    Call<List<EEG_Values>> getValues(@Query("name")String name);

    @POST("eeg_values")
    Call<EEG_Values> createPost(@Body EEG_Values eeg_values);






}
