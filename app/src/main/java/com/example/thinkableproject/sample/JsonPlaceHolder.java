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

    //===== Posting Raw EEG values for concentration and relaxation===

    @POST("brain_waves")
    Call<List> PostBrainWaveData(@Body List brain_waves);


    @POST("relaxation_index")
    Call<List> PostRelaxationData(@Body ArrayList relaxatinData);

    @POST("concentration_index")
    Call<List> PostConcentrationData(@Body ArrayList brain_waves);
    //=================================================================

    //==== Posting ColorPattern Data==================================

    @POST("concentration_index")
    Call<List> PostColorPatternConData(@Body ArrayList brain_waves);

    @POST("relaxation_index")
    Call<List> PostColorPatternRelData(@Body ArrayList relaxatinData);
//====================================================================

    //=========Posting CardGame Data=================================
    @POST("concentration_index")
    Call<List> PostCardGameConData(@Body ArrayList brain_waves);

    @POST("relaxation_index")
    Call<List> PostCardGameRelData(@Body ArrayList relaxatinData);
//    ================================================================

    @FormUrlEncoded
    @POST("")
    Call<Void> createPostVal(
            @Field("name") String name,
            @Field("age") String age,
            @Field("school") String school
    );


}
