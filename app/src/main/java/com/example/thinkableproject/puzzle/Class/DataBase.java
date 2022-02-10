package com.example.thinkableproject.puzzle.Class;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class DataBase extends AppCompatActivity {
    public SharedPreferences preferencesName ,preferencesScore,preferencesCounter;
    private Context mContext;
    public SharedPreferences.Editor editorName,editorScore,editorCounter;


    public DataBase(Context context){
        mContext=context;
    }

    public void setPrefRef(String namePref, String scorePref){
        preferencesName = mContext.getSharedPreferences(namePref,0);
        editorName=preferencesName.edit();
        preferencesScore = mContext.getSharedPreferences(scorePref,0);
        editorScore=preferencesScore.edit();
        editorName.apply();
        editorScore.apply();
        preferencesCounter = mContext.getSharedPreferences("counterPref",0);
        editorCounter = preferencesCounter.edit();
        editorCounter.apply();
    }

    public void setValues(String name, int score, int context){
        int Game9Index,Game15Index,Game24Index;
        switch (context) {
            case 1:
                Game9Index=preferencesCounter.getInt("game9counter",0);
                editorName.putString(""+Game9Index,name);
                editorScore.putInt(""+Game9Index,score);
                Game9Index++;
                editorCounter.putInt("game9counter",Game9Index);
                break;
            case 2:
                Game15Index=preferencesCounter.getInt("game15counter",0);
                editorName.putString(""+Game15Index,name);
                editorScore.putInt(""+Game15Index,score);
                Game15Index++;
                editorCounter.putInt("game15counter",Game15Index);
                break;
            case 3:
                Game24Index=preferencesCounter.getInt("game24counter",0);
                editorName.putString(""+Game24Index,name);
                editorScore.putInt(""+Game24Index,score);
                Game24Index++;
                editorCounter.putInt("game24counter",Game24Index);
                break;

        }
        editorName.apply();
        editorScore.apply();
        editorCounter.apply();

    }

    public int getMaxScore(int context,String db) {
        preferencesScore = mContext.getSharedPreferences(db,0);
        int bestScore=preferencesScore.getInt(""+0, 0),size=0;
        switch (context){
            case 1:
                size=preferencesCounter.getInt("game9counter",0);
                break;
            case 2:
                size=preferencesCounter.getInt("game15counter",0);
                break;
            case 3:
                size=preferencesCounter.getInt("game24counter",0);
                break;
        }
        for(int i=0;i<size;i++) {
            preferencesScore.getInt("" + i, 0);
            if (bestScore>preferencesScore.getInt(""+i, 0))
                bestScore=preferencesScore.getInt(""+i, 0);


        }
        return bestScore;
    }

    public int checkIfScoreIsBest(String pref,int newScore) {
        preferencesScore = mContext.getSharedPreferences(pref,0);
        int[] valueArr = new int[10];
        int[] keyArr = new int[10];
        int z=0;
        while (z < 10) {
            valueArr[z] = preferencesScore.getInt("" + z, 0);
            keyArr[z] = z;
            z++;
        }
        for (int i = 0; i < valueArr.length; i++) {
            for (int j = i + 1; j < valueArr.length; j++) {
                int tmp;
                if (valueArr[i] > valueArr[j]) {
                    tmp = valueArr[i];
                    valueArr[i] = valueArr[j];
                    valueArr[j] = tmp;
                    tmp = keyArr[i];
                    keyArr[i] = keyArr[j];
                    keyArr[j] = tmp;
                }
            }
        }
        if (newScore<valueArr[9])
            return keyArr[9];
        return -1;
    }

    public void changeValues(String name, int score, int checkPlace) {
        editorName.putString("" + checkPlace, name);
        editorScore.putInt("" + checkPlace, score);
        editorName.apply();
        editorScore.apply();
        editorCounter.apply();
    }
}
