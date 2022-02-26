package com.example.thinkableproject.WordMatching;

import java.util.Arrays;
import java.util.Random;

public class GameUtilities {
    private String[] grid;
    private String[] list;
    private int[] map;
    public int numOfPairs;
    public int numOfButtons;
    public final static int wordSize = 20;
    private String firstGuess = "";
    private String secondGuess = "";
    private int firstGuessIndex = -1;
    private int secondGuessIndex = -1;
    private boolean[] clickable;
    private int buttonsLeft;
    public int par = GlobalElements.getPar(GlobalElements.getInstance().getLevel());
    private int movesUsed = 0;
    public GameUtilities(int buttons){
        numOfButtons = buttons;
        numOfPairs = GlobalElements.getInstance().getKeySet().getNumOfPairs();
        clickable = new boolean[numOfButtons];
        Arrays.fill(clickable,true);
        buttonsLeft = buttons;
        list = new String[numOfButtons*2];
        map = new int[list.length];
        grid = new String[numOfButtons];
        generateList();
        generateMap();
        generateGrid();
    }
    public void generateList(){
        for(int i = 0;i<numOfPairs;i++){
            list[i] = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(i);
        }
        for(int i = numOfPairs;i<numOfPairs*2;i++){
            list[i] = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(i-numOfPairs);
        }
    }
    public void generateMap(){
        switch(numOfButtons){
            case 4:
                generateMapForFirstGrid();
                break;
            case 6:
                generateMapForSecondGrid();
                break;
            case 8:
                generateMapForThirdGrid();
                break;
            case 10:
                generateMapForFourthGrid();
                break;
            case 12:
                generateMapForFifthGrid();
                break;
        }
    }
    public void generateMapForFirstGrid(){
        switch(numOfPairs){
            case 1:
                Arrays.fill(map,2);
                break;
            case 2:
                Arrays.fill(map,1);
                break;
        }
    }
    public void generateMapForSecondGrid(){
        switch(numOfPairs){
            case 1:
                Arrays.fill(map,3);
                break;
            case 2:
                Random rand = new Random();
                int flip = rand.nextInt(2);
                if(flip==0){
                    map[0] = 1;
                    map[1] = 2;
                    map[2] = 1;
                    map[3] = 2;
                }
                else if(flip==1){
                    map[0] = 2;
                    map[1] = 1;
                    map[2] = 2;
                    map[3] = 1;
                }
                break;
            case 3:
                Arrays.fill(map,1);
                break;
        }
    }
    public void generateMapForThirdGrid(){
        switch(numOfPairs){
            case 1:
                Arrays.fill(map,4);
                break;
            case 2:
                Arrays.fill(map,2);
                break;
            case 3:
                Arrays.fill(map,1);
                Random rand = new Random();
                int toss = rand.nextInt(3);
                switch(toss){
                    case 0:
                        map[0] = 2;
                        map[3] = 2;
                        break;
                    case 1:
                        map[1] = 2;
                        map[4] = 2;
                        break;
                    case 2:
                        map[2] = 2;
                        map[5] = 2;
                        break;
                }
                break;
            case 4:
                Arrays.fill(map,1);
        }
    }
    public void generateMapForFourthGrid(){
        switch(numOfPairs){
            case 1:
                Arrays.fill(map,5);
                break;
            case 2:
                Arrays.fill(map,2);
                Random rand = new Random();
                int flip = rand.nextInt(2);
                if(flip==0){
                    map[0]++;
                    map[2]++;
                }
                else if(flip==1){
                    map[1]++;
                    map[3]++;
                }
                break;
            case 3:
                Arrays.fill(map,2);
                Random rand2 = new Random();
                int roll = rand2.nextInt(3);
                if(roll==0){
                    map[0]--;
                    map[3]--;
                }
                else if(roll==1){
                    map[1]--;
                    map[4]--;
                }
                else if(roll==2){
                    map[2]--;
                    map[5]--;
                }
                break;
            case 4:
                Arrays.fill(map,1);
                Random rand3 = new Random();
                int roll2 = rand3.nextInt(4);
                if(roll2==0){
                    map[0]++;
                    map[4]++;
                }
                else if(roll2==1){
                    map[1]++;
                    map[5]++;
                }
                else if(roll2==2){
                    map[2]++;
                    map[6]++;
                }
                else if(roll2==3){
                    map[3]++;
                    map[7]++;
                }
                break;
            case 5:
                Arrays.fill(map,1);
                break;
        }
    }
    public void  generateMapForFifthGrid(){
        switch(numOfPairs){
            case 1:
                Arrays.fill(map,6);
                break;
            case 2:
                Arrays.fill(map,3);
                break;
            case 3:
                Arrays.fill(map,2);
                break;
            case 4:
                Arrays.fill(map,1);
                int roll3 = (new Random()).nextInt(2);
                if(roll3==0){
                    map[0]++;
                    map[4]++;
                    map[1]++;
                    map[5]++;
                }
                else if(roll3==1){
                    map[2]++;
                    map[6]++;
                    map[3]++;
                    map[7]++;
                }
                break;
            case 5:
                Arrays.fill(map,1);
                int roll4 = (new Random()).nextInt(5);
                if(roll4==0){
                    map[0]++;
                    map[5]++;
                }
                else if(roll4==1){
                    map[1]++;
                    map[6]++;
                }
                else if(roll4==2){
                    map[2]++;
                    map[7]++;
                }
                else if(roll4==3){
                    map[3]++;
                    map[8]++;
                }
                else if(roll4==4){
                    map[4]++;
                    map[9]++;
                }
                break;
            case 6:
                Arrays.fill(map,1);
                break;
        }
    }
    public String getRandomWord(){
        Random rand = new Random();
        int index = rand.nextInt(numOfPairs*2);
        while(map[index]==0){
            index = rand.nextInt(numOfPairs*2);
        }
        map[index]--;
        return list[index];
    }
    public void generateGrid(){
        for(int i = 0;i<numOfButtons;i++){
            grid[i] = getRandomWord();
        }
    }
    public void setGuess(int index){
        if(firstGuess.equals("")){
            firstGuess = grid[index];
            firstGuessIndex = index;
        }
        else if(secondGuess.equals("")){
            secondGuess = grid[index];
            secondGuessIndex = index;
        }
    }
    public boolean fullyGuessed(){
        if(!firstGuess.equals("")&&!secondGuess.equals("")){
            return true;
        }
        return false;
    }
    public String getWordAt(int index){
        return grid[index];
    }
    public boolean isClickable(int index){
        return clickable[index];
    }
    public boolean matches(){
        return GlobalElements.getInstance().getKeySet().matches(firstGuess,secondGuess);
    }
    public void setClickableFalse(int index){
        clickable[index] = false;
    }
    public int getFirstGuessIndex(){
        return firstGuessIndex;
    }
    public int getSecondGuessIndex(){
        return secondGuessIndex;
    }
    public void updateButtonsLeft(){
        buttonsLeft-=2;
    }
    public int getButtonsLeft(){
        return buttonsLeft;
    }
    public void resetGuess(){
        firstGuess = "";
        secondGuess = "";
        firstGuessIndex = -1;
        secondGuessIndex = -1;
    }
    public void updateMovesUsed(){
        movesUsed++;
    }
    public int getMovesUsed(){
        return movesUsed;
    }
    public int getQuestionMarkSize(){
        switch(numOfButtons){
            case 4:
                return 150;
            case 6:
                return 100;
            case 8:
                return 60;
            case 10:
                return 50;
            case 12:
                return 40;
        }
        return 0;
    }
    public boolean canGuess(){
        if(secondGuess.equals("")){
            return true;
        }
        return false;
    }
}
