package com.example.thinkableproject.sample;

import com.example.EEG_Values;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface JsonPlaceHolder {


    @FormUrlEncoded
    @POST("memory")
    Call<Void> PostMemoryData(
            @Field("alpha") int alpha,
            @Field("beta") int beta,
            @Field("gamma") int gamma,
            @Field("theta") int theta,
            @Field("delta") int delta
    );

    @POST("brain_waves")
    Call<List> PostBrainWaveData(@Body List brain_waves);

//    @POST("calibration")
//    Call<List> PostCalibrationData(@Body ArrayList brain_waves);
//
//
//
//    @POST("concentration")
//    Call<Object> PostConcentrationData(@Body Object concentration);
//
//    @POST("relaxation")
//    Call<Object> PostRelaxationData(@Body Object relaxation);

//    @POST("memory")
//    Call<Object> PostMemoryData(@Body Object memory);


    @POST("relaxation_index")
    Call<List> PostRelaxationData(@Body ArrayList relaxatinData);

    @POST("concentration_index")
    Call<List> PostConcentrationData(@Body ArrayList brain_waves);

    @FormUrlEncoded
    @POST("")
    Call<Void> createPostVal(
            @Field("name") String name,
            @Field("age") String age,
            @Field("school") String school
    );



}
