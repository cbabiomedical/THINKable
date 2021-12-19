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

//    @GET("concentration_values")
//    Call<List<Concentration_Values>>  getCValues(@Query("name1")String name);
//
//    @POST("concentration_values")
//    Call<Concentration_Values> createPost(@Body Concentration_Values concentration_values);
//
//    @GET("relaxation_values")
//    Call<List<Relaxation_Values>>  getRValues(@Query("name2")String name);
//
//    @POST("relaxation_values")
//    Call<Relaxation_Values> createPost(@Body Relaxation_Values relaxation_values);
//
//    @GET("calibration")
//    Call<List<Relaxation_Values>>  getcalValues(@Query("name2")String name);
//
//    @POST("relaxation_values")
//    Call<Relaxation_Values> createPost(@Body Relaxation_Values relaxation_values);


}
