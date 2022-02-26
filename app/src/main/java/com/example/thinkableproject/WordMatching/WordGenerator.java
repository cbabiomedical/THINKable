package com.example.thinkableproject.WordMatching;

import java.util.Random;

public class WordGenerator {
    private static final String[] wordList =
            {"CAT","DOG","PLANE","COMPUTE","INTERNET","HISTORY","PUMPKIN","KINSMEN","SYSTEM","ROBOT","PROSPECT",
                    "INTUITION","SUPER","CRYPT","DINNER","SLED","DRAGON","TIGER","UNIVERSE","CORNY"};
    private static Random rand = new Random();
    public static String getRandomWord(){
        int total = wordList.length;
        return wordList[rand.nextInt(total)];
    }
}
