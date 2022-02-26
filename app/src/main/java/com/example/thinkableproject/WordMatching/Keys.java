package com.example.thinkableproject.WordMatching;

import java.util.ArrayList;

public class Keys {
    private ArrayList<String> firstKeyList;
    private ArrayList<String> secondKeyList;
    private int numOfPairs;
    public Keys(int level){
        numOfPairs = getNumOfPairsNeeded(level);
        firstKeyList = new ArrayList<String>();
        secondKeyList = new ArrayList<String>();
        generateFirstList(numOfPairs);
        generateSecondList(numOfPairs);
    }
    public void generateFirstList(int sizeOfList){
        for(int i = 0;i<sizeOfList;i++){
            String candidateWord = WordGenerator.getRandomWord();
            while(firstKeyList.contains(candidateWord) || secondKeyList.contains(candidateWord)){
                candidateWord = WordGenerator.getRandomWord();
            }
            firstKeyList.add(candidateWord);
        }
    }
    public void generateSecondList(int sizeOfList){
        for(int i = 0;i<sizeOfList;i++){
            String candidateWord = WordGenerator.getRandomWord();
            while(firstKeyList.contains(candidateWord) || secondKeyList.contains(candidateWord)){
                candidateWord = WordGenerator.getRandomWord();
            }
            secondKeyList.add(candidateWord);
        }
    }
    public int getNumOfPairsNeeded(int level){
        return GlobalElements.getNumOfKeyPairsNeeded(level);
    }
    public String getKeyFromFirstList(int index){
        return firstKeyList.get(index);
    }
    public String getKeyFromSecondList(int index){
        return secondKeyList.get(index);
    }
    public int getNumOfPairs(){
        return numOfPairs;
    }
    public boolean matches(String a,String b){
        if(firstKeyList.contains(a)){
            if(firstKeyList.indexOf(a)==secondKeyList.indexOf(b)){
                return true;
            }
            return false;
        }
        else if(secondKeyList.contains(a)){
            if(secondKeyList.indexOf(a)==firstKeyList.indexOf(b)){
                return true;
            }
            return false;
        }
        return false;
    }
}
