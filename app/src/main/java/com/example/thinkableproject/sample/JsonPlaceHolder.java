package com.example.thinkableproject.sample;

import com.example.EEG_Values;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface JsonPlaceHolder {
    @GET("eeg_values")
    Call<List<EEG_Values>> getValues();

    @GET("concentration")
    Call<List<Concentration>> getConcentrationValues();

    @GET("relaxation")
    Call<List<Relaxation>> getRelaxationValues();

    @GET("brain_waves")
    Call<List<Brain_Waves>> getBrainWavesValues();

    @FormUrlEncoded
    @POST("brain_waves")
    Call<Void> PostBrainWavesData(
            @Field("alpha") int alpha,
            @Field("beta") int beta,
            @Field("gamma") int gamma,
            @Field("theta") int theta,
            @Field("delta") int delta
    );

    @FormUrlEncoded
    @POST("relaxation")
    Call<Void> PostRelaxationData(
            @Field("alpha") int alpha,
            @Field("beta") int beta,
            @Field("gamma") int gamma,
            @Field("theta") int theta,
            @Field("delta") int delta
    );

    @FormUrlEncoded
    @POST("concentration")
    Call<Void> PostConcentrationData(
            @Field("alpha") int alpha,
            @Field("beta") int beta,
            @Field("gamma") int gamma,
            @Field("theta") int theta,
            @Field("delta") int delta
    );

    @FormUrlEncoded
    @POST("eeg_values")
    Call<Void> PostData(
            @Field("alpha") int alpha,
            @Field("beta") int beta,
            @Field("gamma") int gamma,
            @Field("theta") int theta,
            @Field("delta") int delta
    );


    @FormUrlEncoded
    @POST("/post")
    Call<Void> createPostVal(
            @Field("name") String name,
            @Field("age") String age,
            @Field("school") String school
    );



    @GET("/post")
    Call<List<Post>> getPost();
}
