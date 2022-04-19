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

    //==== Posting ColorPattern Data==================================

    @POST("memory_index")
    Call<List> PostColorPatternMemData(@Body ArrayList brain_waves);
    
//====================================================================

    //=========Posting CardGame Data=================================

    @POST("memory_index")
    Call<List> PostCardGamData(@Body ArrayList brain_waves);
//    ================================================================

    //=========Posting SpaceHooter Data=================================

    @POST("concentration_index")
    Call<List> PostSpaceHooterData(@Body ArrayList brain_waves);
//    ================================================================

    //=========Posting DuckHunt Data=================================

    @POST("concentration_index")
    Call<List> PostDuckHuntData(@Body ArrayList brain_waves);
//    ================================================================
    //=========Posting PianoTiles Data=================================

    @POST("concentration_index")
    Call<List> PostPianoTilesData(@Body ArrayList brain_waves);
//    ================================================================
    //=========Posting NinjaDart Data=================================

    @POST("concentration_index")
    Call<List> PostNinjaDartData(@Body ArrayList brain_waves);
//    ================================================================

//=========Posting Puzzles Data=================================

    @POST("memory_index")
    Call<List> PostPuzzlesData(@Body ArrayList brain_waves);
//    ================================================================

    //=========Posting Fly Data=================================

    @POST("concentration_index")
    Call<List> PostFlyData(@Body ArrayList brain_waves);
//    ================================================================
    //=========Posting Advanced Puzzle Data=================================

    @POST("concentration_index")
    Call<List> PostAdvancedPuzzleData(@Body ArrayList brain_waves);
//    ================================================================

    //=========Posting Word Matching Data=================================

    @POST("concentration_index")
    Call<List> PostWordMatchData(@Body ArrayList brain_waves);
//    ================================================================
    //=========Posting Word Matching Data=================================

    @POST("relaxation_index")
    Call<List> PostMusicData(@Body ArrayList brain_waves);
//    ================================================================
    //=========Posting Word Matching Data=================================

    @POST("relaxation_index")
    Call<List> PostMeditationData(@Body ArrayList brain_waves);
//    ================================================================
    //=========Posting Word Matching Data=================================

    @POST("relaxation_index")
    Call<List> PostVideoData(@Body ArrayList brain_waves);
//    ================================================================





    @FormUrlEncoded
    @POST("")
    Call<Void> createPostVal(
            @Field("name") String name,
            @Field("age") String age,
            @Field("school") String school
    );

    @POST("concentration_times")
    Call<List> PostTimeToConcentrate(@Body ArrayList brain_waves);



//====================================================================


}
