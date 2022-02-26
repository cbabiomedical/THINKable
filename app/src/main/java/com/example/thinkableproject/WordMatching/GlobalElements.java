package com.example.thinkableproject.WordMatching;

public class GlobalElements {
    private  Keys keys;
    private static GlobalElements instance;
    private int level;
    private int score = 0;
    private int prevScore;
    private static int[] numOfKeyPairsNeeded = {1,2,1,2,3,1,2,3,4,1,2,3,4,5,1,2,3,4,5,6};
    public static GlobalElements getInstance(){
        if(instance==null){
            instance = new GlobalElements();
        }
        return instance;
    }
    public  void resetKeySet(int level){
        keys = new Keys(level);
    }
    public  Keys getKeySet(){
        return keys;
    }
    public void setLevel(int lev){
        level = lev;
    }
    public int getLevel(){
        return level;
    }
    public void resetLevel(){
        setLevel(1);
    }
    public void levelUp(){
        level++;
    }
    public static int getNumOfKeyPairsNeeded(int level){
        return numOfKeyPairsNeeded[level-1];
    }
    public void resetScore(){
        setScore(0);
    }
    public void setScore(int x){
        prevScore = score;
        score = x;
    }
    public void init(int lev, int sco){
        setLevel(lev);
        setScore(sco);
    }
    public int getScore(){
        return score;
    }
    public void addToScore(int x){
        setScore(getScore()+Math.max(0,x));
    }
    public int getScoreDifference(){
        return score-prevScore;
    }
    public static int getPar(int level){
        if(level == 1 || level == 2){
            return 4;
        }
        else if(level >= 3 && level<= 5){
            return 6;
        }
        else if(level >= 6 && level <= 9){
            return 8;
        }
        else if(level >= 10 && level <= 14){
            return 10;
        }
        else if(level >= 15 && level <= 20){
            return 12;
        }
        return 0;
    }
}
