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
//    @GET("brain_waves")
//    Call<List<EEG_Values>> getValues();
//
//    @GET("concentration")
//    Call<List<Concentration>> getConcentrationValues();
//
//    @GET("relaxation")
//    Call<List<Relaxation>> getRelaxationValues();
//
//    @GET("brain_waves")
//    Call<List<Brain_Waves>> getBrainWavesValues();
//
//    @GET("memory")
//    Call<List<Memory>> getMemoryValues();

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

    @POST("calibration")
    Call<List> PostCalibrationData(@Body ArrayList brain_waves);


//    @FormUrlEncoded
//    @POST("relaxation")
//    Call<Void> PostRelaxationData(
//            @Field("alpha") int alpha,
//            @Field("beta") int beta,
//            @Field("gamma") int gamma,
//            @Field("theta") int theta,
//            @Field("delta") int delta
//    );

//    @FormUrlEncoded
//    @POST("concentration")
//    Call<Void> PostConcentrationData(
//            @Field("delta") Object delta,
//            @Field("gamma") Object theta,
//            @Field("alpha") Object alpha,
//            @Field("beta") Object beta,
//            @Field("theta") Object gamma
//    );

    @POST("concentration")
    Call<Object> PostConcentrationData(@Body Object concentration);

    @POST("relaxation")
    Call<Object> PostRelaxationData(@Body Object relaxation);

    @POST("memory")
    Call<Object> PostMemoryData(@Body Object memory);

//    @FormUrlEncoded
//    @POST("eeg_values")
//    Call<Void> PostData(
//            @Field("alpha") int alpha,
//            @Field("beta") int beta,
//            @Field("gamma") int gamma,
//            @Field("theta") int theta,
//            @Field("delta") int delta
//    );


    @FormUrlEncoded
    @POST("")
    Call<Void> createPostVal(
            @Field("name") String name,
            @Field("age") String age,
            @Field("school") String school
    );


//    @GET("")
//    Call<List<Post>> getPost();
}
